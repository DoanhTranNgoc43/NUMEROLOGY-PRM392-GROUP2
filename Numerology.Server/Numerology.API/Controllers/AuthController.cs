using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Numerology.Core.Constants;
using Numerology.Core.Interfaces;
using Numerology.Core.Models;
using Numerology.Core.Models.Configs;
using Numerology.Core.Models.DTOs.Auth;

namespace Numerology.API.Controllers;

[Route("api/[controller]")]
[ApiController]
public class AuthController(IAuthService authService, IIdentityService identityService,
IUserService userService, IOptions<JwtConfig> jwtConfig) : ControllerBase
{
    private readonly IAuthService _authService = authService;
    private readonly IIdentityService _identityService = identityService;
    private readonly IUserService _userService = userService;
    private readonly JwtConfig _jwtConfig = jwtConfig.Value;
    [HttpPost("login")]
    public async Task<IActionResult> Authenticate([FromBody] LoginDTO loginDTO)
    {
        try
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);

            var result = await _authService.SigninUserAsync(loginDTO);
            if (!result) throw new Exception("Email or password is incorrect");

            // Get token data and user information
            var tokenData = await _authService.CreateAuthTokenAsync(loginDTO.Username);
            var user = await _authService.GetUserByUsernameAsync(loginDTO.Username);

            return Ok(new Response
            {
                Status = ResponseStatus.SUCCESS,
                Message = "Login successful",
                Data = new
                {
                    token = tokenData.AccessToken,
                    user = new
                    {
                        id = user.Id,
                        name = user.UserName,
                        email = user.Email
                    }
                }
            });
        }
        catch (Exception ex)
        {
            return BadRequest(new Response
            {
                Status = ResponseStatus.ERROR,
                Message = ex.Message
            });
        }
    }
     [HttpPost("login-google")]
    public async Task<IActionResult> GoogleAuthenticate(ExternalAuthDTO externalAuth)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        var user = await _userService.FindOrCreateUserAsync(externalAuth, [UserRole.SUB_AGENT]);
        var tokenDTO = await _userService
            .CreateAuthTokenAsync(user!.Username, _jwtConfig.RefreshTokenValidityInDays);
        return Ok(new Response
        {
            Status = ResponseStatus.SUCCESS,
            Message = "Login successfully"
        });
    }

    [HttpPost("register")]
    public async Task<IActionResult> Register([FromBody] RegisterDTO registerDTO)
    {
        try
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);
            var (isUserExists, isConfirmed) = await _authService.CheckUserExistsWithEmailConfirmedAsync(registerDTO.Email);
            if (isUserExists && isConfirmed)
            {
                return BadRequest(new Response
                {
                    Status = ResponseStatus.ERROR,
                    Message = "Email already exists and is confirmed"
                });
            }
            if (isUserExists && !isConfirmed)
            {
                var cleanupResult = await _authService.CleanupUnconfirmedUserAsync(registerDTO.Email);
                if (!cleanupResult)
                {
                    return BadRequest(new Response
                    {
                        Status = ResponseStatus.ERROR,
                        Message = "Failed to cleanup unconfirmed user"
                    });
                }
            }
            if (_authService.CheckUserNameExists(registerDTO.Username).Result)
            {
                return BadRequest(new Response
                {
                    Status = ResponseStatus.ERROR,
                    Message = "Username already exists"
                });
            }
            var role = await _identityService.GetRoleId(UserRole.GENERAL_AGENT);
            var isSucceed = await _authService.CreateUserAsync(registerDTO, role);
            if (!isSucceed)
            {
                return BadRequest(new Response
                {
                    Status = ResponseStatus.ERROR,
                    Message = "Failed to create user"
                });
            }
            return Ok(new Response
            {
                Status = ResponseStatus.SUCCESS,
                Message = "Registration successful, please check your email to confirm your account",
            });
        }
        catch (Exception ex)
        {
            return BadRequest(new Response
            {
                Status = ResponseStatus.ERROR,
                Message = ex.Message
            });
        }
    }
    [Authorize]
    [HttpGet("/api/user/profile")]
    public async Task<IActionResult> GetProfile()
    {
        var username = User.FindFirstValue(ClaimTypes.Name);
        if (string.IsNullOrEmpty(username))
        {
            return Unauthorized(new Response
            {
                Status = ResponseStatus.ERROR,
                Message = "Unauthorized"
            });
        }

        var profile = await _authService.GetUserProfileAsync(username);
        return Ok(profile);
    }
}