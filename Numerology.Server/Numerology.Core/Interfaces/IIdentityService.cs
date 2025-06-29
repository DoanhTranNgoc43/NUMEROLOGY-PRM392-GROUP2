
namespace Numerology.Core.Interfaces;

public interface IIdentityService
{
    Task<bool> CreateRoleAsync(string roleName);
    Task<string> GetRoleId(string roleName);
}