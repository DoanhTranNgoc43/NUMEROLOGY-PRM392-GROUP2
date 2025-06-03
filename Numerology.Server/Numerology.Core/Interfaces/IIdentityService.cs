using Numerology.Core.Models.DTOs.Auth;

namespace Numerology.Core.Interfaces;

public interface IIdentityService
{
    Task<bool> SigninUserAsync(LoginDTO loginDTO);
}