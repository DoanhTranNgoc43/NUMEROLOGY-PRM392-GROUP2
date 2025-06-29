using Numerology.Core.Models.DTOs.Auth;
using Numerology.Core.Models.DTOs.Token;
using Numerology.Core.Models.DTOs.User;


namespace Numerology.Core.Interfaces;

public interface IUserService
{
    Task<UserDTO?> FindOrCreateUserAsync(ExternalAuthDTO externalAuth, List<string> roles);
    Task<TokenDTO> CreateAuthTokenAsync(string userName, int expDays = -1);
}