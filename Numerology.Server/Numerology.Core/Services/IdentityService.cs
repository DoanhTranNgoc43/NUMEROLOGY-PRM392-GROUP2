using Microsoft.AspNetCore.Identity;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.Entities;


namespace Numerology.Core.Services;

public class IdentityService(UserManager<User> userManager, SignInManager<User> signInManager,
RoleManager<IdentityRole> roleManager) : IIdentityService
{
    private readonly UserManager<User> _userManager = userManager;
    private readonly SignInManager<User> _signInManager = signInManager;
    private readonly RoleManager<IdentityRole> _roleManager = roleManager;

    public async Task<bool> CreateRoleAsync(string roleName)
    {
        var result = await _roleManager.CreateAsync(new IdentityRole(roleName));
        if (!result.Succeeded)
        {
            throw new Exception($"Role creation failed: {string.Join(", ", result.Errors.Select(e => e.Description))}");
        }
        return result.Succeeded;
    }
    public async Task<string> GetRoleId(string roleName)
    {
        if (string.IsNullOrEmpty(roleName))
        {
            throw new ArgumentException("Role name cannot be null or empty.", nameof(roleName));
        }
        var role = await _roleManager.FindByNameAsync(roleName) ?? throw new Exception($"Role '{roleName}' not found.");
        return role.Id;
    }
}