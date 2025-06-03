using Microsoft.AspNetCore.Mvc;
using Numerology.Core.Constants;
using Numerology.Core.Interfaces;
using Numerology.Core.Models;
using Numerology.Core.Models.DTOs.Auth;

namespace Numerology.API.Controllers;

[Route("api/[controller]")]
[ApiController]
public class AuthController(IIdentityService identityService) : ControllerBase
{
    private readonly IIdentityService _identityService = identityService;
    [HttpPost("login")]
    public async Task<IActionResult> Authenticate([FromBody] LoginDTO loginDTO)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        try
        {
            var result = await _identityService.SigninUserAsync(loginDTO);
            if (!result) throw new Exception("Email or password is incorrect");
            return Ok(new Response
            {
                Status = ResponseStatus.SUCCESS,
                Message = "Login successful",
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
}