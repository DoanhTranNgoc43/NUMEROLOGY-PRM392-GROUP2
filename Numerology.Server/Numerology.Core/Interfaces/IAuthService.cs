using Numerology.Core.Models.DTOs.Auth;
using Numerology.Core.Models.DTOs.Token;
using Numerology.Core.Models.Entities;

namespace Numerology.Core.Interfaces;

public interface IAuthService
{
    Task<bool> SigninUserAsync(LoginDTO loginDTO);
    Task<bool> ConfirmEmailAsync(string email, string token);
    Task<bool> CheckUserNameExists(string username);
    Task<(bool isUserExists, bool isConfirmed)> CheckUserExistsWithEmailConfirmedAsync(string email);
    Task<bool> CleanupUnconfirmedUserAsync(string email);
    Task<bool> CreateUserAsync(RegisterDTO registerDTO, string roles, bool confirm = false);
    Task<TokenDTO> CreateAuthTokenAsync(string username, int expDays = -1);
    Task<User> GetUserByUsernameAsync(string username);
    Task<UserProfileDTO> GetUserProfileAsync(string username);
    Task<UserProfileDTO> UpdateUserProfileAsync(string username, UpdateProfileDTO updateProfileDTO);
}