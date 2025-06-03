using Microsoft.AspNetCore.Identity;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.DTOs.Auth;
using Numerology.Core.Models.Entities;

namespace Numerology.Core.Services;

public class IdentityService(UserManager<User> userManager, SignInManager<User> signInManager,
RoleManager<IdentityRole> roleManager) : IIdentityService
{
    private readonly UserManager<User> _userManager = userManager;
    private readonly SignInManager<User> _signInManager = signInManager;
    private readonly RoleManager<IdentityRole> _roleManager = roleManager;
    public async Task<bool> SigninUserAsync(LoginDTO loginDTO)
    {
        if (string.IsNullOrEmpty(loginDTO.Email))
        {
            throw new Exception("Email cannot be null or empty.");
        }
        if (string.IsNullOrEmpty(loginDTO.Password))
        {
            throw new Exception("Password cannot be null or empty.");
        }
        var result = await _signInManager.PasswordSignInAsync(
            loginDTO.Email,
            loginDTO.Password,
           true,
           false);
        return result.Succeeded;
    }
}