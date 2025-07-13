using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using Numerology.Core.Exceptions;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.Configs;
using Numerology.Core.Models.DTOs.Auth;
using Numerology.Core.Models.DTOs.Token;
using Numerology.Core.Models.Entities;
using Numerology.Core.Repositories;

namespace Numerology.Core.Services;

public class AuthService : IAuthService
{
    private readonly JwtConfig _jwtConfig;
    private readonly SymmetricSecurityKey _key;
    private readonly SignInManager<User> _signInManager;
    private readonly UserManager<User> _userManager;
    private readonly IUserRepository _userRepository;

    public AuthService(IOptions<JwtConfig> jwtConfig, SignInManager<User> signInManager,
     UserManager<User> userManager, IUserRepository userRepository)
    {
        _jwtConfig = jwtConfig.Value;
        _key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtConfig.SigningKey));
        _signInManager = signInManager;
        _userManager = userManager;
        _userRepository = userRepository;
    }

    public async Task<bool> SigninUserAsync(LoginDTO loginDTO)
    {
        if (string.IsNullOrEmpty(loginDTO.Username))
        {
            throw new Exception("Username cannot be null or empty.");
        }
        if (string.IsNullOrEmpty(loginDTO.Password))
        {
            throw new Exception("Password cannot be null or empty.");
        }
        var result = await _signInManager.PasswordSignInAsync(
            loginDTO.Username,
            loginDTO.Password,
           true,
           false);
        return result.Succeeded;
    }

    public async Task<TokenDTO> CreateAuthTokenAsync(string username, int expDays = -1)
    {
        if (string.IsNullOrEmpty(username))
        {
            throw new ArgumentException("Username cannot be null or empty.", nameof(username));
        }
        var user = await _userRepository.GetUserByUsernameAsync(username)
            ?? throw new NotFoundException($"User not found with username: {username}");
        user.RefreshToken = GenerateRefreshToken();
        if (expDays > 0) user.RefreshTokenExpiryTime = DateTime.Now.AddDays(expDays);
        await _userManager.UpdateAsync(user);
        var claims = new List<Claim>
        {
             new(ClaimTypes.Name, user.UserName??string.Empty),
             new(ClaimTypes.Email, user.Email??string.Empty),
             new(ClaimTypes.Role, user.Role!.Name ?? string.Empty),
        };
        return new TokenDTO()
        {
            AccessToken = GenerateAccessToken(claims),
            RefreshToken = user.RefreshToken
        };
    }

    public async Task<User> GetUserByUsernameAsync(string username)
    {
        if (string.IsNullOrEmpty(username))
        {
            throw new ArgumentException("Username cannot be null or empty.", nameof(username));
        }
        return await _userRepository.GetUserByUsernameAsync(username)
            ?? throw new NotFoundException($"User not found with username: {username}");
    }

    private string GenerateAccessToken(List<Claim> claims)
    {
        var credentials = new SigningCredentials(_key, SecurityAlgorithms.HmacSha256);
        var tokenDescriptor = new SecurityTokenDescriptor
        {
            Subject = new(claims),
            Expires = DateTime.Now.AddMinutes(_jwtConfig.TokenValidityInMinutes),
            SigningCredentials = credentials,
            Issuer = _jwtConfig.Issuer,
            Audience = _jwtConfig.Audience
        };
        var tokenHandle = new JwtSecurityTokenHandler();
        var token = tokenHandle.CreateToken(tokenDescriptor);
        return tokenHandle.WriteToken(token);
    }

    public async Task<bool> CheckUserNameExists(string username)
    {
        if (string.IsNullOrEmpty(username))
        {
            throw new ArgumentException("Username cannot be null or empty.", nameof(username));
        }
        return await _userManager.FindByNameAsync(username) != null;
    }

    public async Task<(bool isUserExists, bool isConfirmed)> CheckUserExistsWithEmailConfirmedAsync(string email)
    {
        var user = await _userManager.FindByEmailAsync(email);
        if (user != null)
        {
            return (true, user.EmailConfirmed);
        }
        return (false, false);
    }

    public async Task<bool> ConfirmEmailAsync(string email, string token)
    {
        var user = await _userManager.FindByEmailAsync(email)
            ?? throw new NotFoundException("User not found by email");
        var result = await _userManager.ConfirmEmailAsync(user, token);
        return result.Succeeded;
    }

    public async Task<bool> CleanupUnconfirmedUserAsync(string email)
    {
        if (string.IsNullOrEmpty(email))
        {
            throw new ArgumentException("Email cannot be null or empty.", nameof(email));
        }
        var user = await _userManager.FindByEmailAsync(email);
        if (user != null && !user.EmailConfirmed)
        {
            var deleteResult = await _userManager.DeleteAsync(user);
            return deleteResult.Succeeded;
        }
        return true;
    }

    public async Task<bool> CreateUserAsync(RegisterDTO registerDTO, string role, bool confirm = false)
    {
        var user = new User
        {
            UserName = registerDTO.Username,
            Email = registerDTO.Email,
            EmailConfirmed = confirm,
            RoleId = role,
        };
        var result = await _userManager.CreateAsync(user, registerDTO.Password);
        if (!result.Succeeded)
        {
            throw new Exception(string.Join("; ", result.Errors.Select(e => e.Description)));
        }
        return result.Succeeded;
    }

    private string GenerateRefreshToken()
    {
        var randomNumber = new byte[64];
        var rng = RandomNumberGenerator.Create();
        rng.GetBytes(randomNumber);
        return Convert.ToBase64String(randomNumber);
    }
    public async Task<UserProfileDTO> GetUserProfileAsync(string username)
    {
        var user = await _userRepository.GetUserByUsernameAsync(username)
            ?? throw new Exception("User not found");

        return new UserProfileDTO
        {
            UserId = user.Id,
            FullName = user.FullName ?? string.Empty,
            Email = user.Email ?? string.Empty,
            PhoneNumber = user.PhoneNumber ?? string.Empty
        };
    }
    public async Task<UserProfileDTO> UpdateUserProfileAsync(string username, UpdateProfileDTO updateProfileDTO)
    {
        var user = await _userRepository.GetUserByUsernameAsync(username)
            ?? throw new NotFoundException($"User not found with username: {username}");

        if (updateProfileDTO.Email != null && updateProfileDTO.Email != user.Email)
        {
            // Check if email is already taken
            var emailExists = await _userManager.FindByEmailAsync(updateProfileDTO.Email) != null;
            if (emailExists)
            {
                throw new InvalidOperationException("Email is already in use");
            }
            user.Email = updateProfileDTO.Email;
            user.EmailConfirmed = false;
        }

        if (updateProfileDTO.FullName != null)
        {
            user.FullName = updateProfileDTO.FullName;
        }

        if (updateProfileDTO.PhoneNumber != null)
        {
            user.PhoneNumber = updateProfileDTO.PhoneNumber;
        }

        user.LastModified = DateTimeOffset.UtcNow;

        var result = await _userManager.UpdateAsync(user);
        if (!result.Succeeded)
        {
            throw new Exception(string.Join("; ", result.Errors.Select(e => e.Description)));
        }

        return new UserProfileDTO
        {
            UserId = user.Id,
            FullName = user.FullName ?? string.Empty,
            Email = user.Email ?? string.Empty,
            PhoneNumber = user.PhoneNumber ?? string.Empty
        };
        
    }
}