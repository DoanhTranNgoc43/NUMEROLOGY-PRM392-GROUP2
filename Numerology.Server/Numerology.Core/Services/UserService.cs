using System.Security.Claims;
using AutoMapper;
using Microsoft.AspNetCore.Identity;
using Numerology.Core.Common;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.DTOs.Auth;
using Numerology.Core.Models.DTOs.Token;
using Numerology.Core.Models.DTOs.User;
using Numerology.Core.Models.Entities;

namespace Numerology.Core.Services;

public class UserService(UserManager<User> userManager, IMapper mapper, ITokenService tokenService) : IUserService
{
    private readonly IMapper _mapper = mapper;
    private readonly UserManager<User> _userManager = userManager;
    private readonly ITokenService _tokenService = tokenService;
    public async Task<Models.DTOs.Auth.UserDTO?> FindOrCreateUserAsync(ExternalAuthDTO externalAuth, List<string> roles)
    {
        var payload = await _tokenService.VerifyGoogleToken(externalAuth)
            ?? throw new Exception("Invalid token");
        if (!EmailHelper.IsMail(payload.Email))
            throw new Exception("Email must be a valid email address.");

        var info = new UserLoginInfo(externalAuth.Provider, payload.Subject, externalAuth.Provider);
        var user = await _userManager.FindByLoginAsync(info.LoginProvider, info.ProviderKey);
        if (user == null)
        {
            user = await _userManager.FindByEmailAsync(payload.Email);
            if (user != null)
                throw new Exception("Email already exists");

            user = new User
            {
                Email = payload.Email,
                UserName = EmailHelper.GetUsername(payload.Email),
                EmailConfirmed = true
            };

            await _userManager.CreateAsync(user);
            await _userManager.AddToRolesAsync(user, roles);
            await _userManager.AddLoginAsync(user, info);
        }
        return _mapper.Map<Models.DTOs.Auth.UserDTO>(user);
    }

    public async Task<TokenDTO> CreateAuthTokenAsync(string userName, int expDays = -1)
    {
        var user = await _userManager.FindByNameAsync(userName)
        ?? throw new Exception("User not found");
        return await CreateAuthTokenAsync(user, expDays);
    }
    private async Task<TokenDTO> CreateAuthTokenAsync(User user, int expDays = -1)
    {
        user.RefreshToken = _tokenService.GenerateRefreshToken();
        if (expDays > 0)
            user.RefreshTokenExpiryTime = DateTime.Now.AddDays(expDays);
        await _userManager.UpdateAsync(user);
        var claims = new List<Claim>
    {
        new(ClaimTypes.Name, user.UserName??string.Empty),
        new(ClaimTypes.Email, user.Email??string.Empty)
    };
        var roles = await _userManager.GetRolesAsync(user);
        foreach (var role in roles)
            claims.Add(new Claim(ClaimTypes.Role, role));
        return new TokenDTO()
        {
            AccessToken = _tokenService.GenerateAccessToken(claims),
            RefreshToken = user.RefreshToken
        };
    }

}