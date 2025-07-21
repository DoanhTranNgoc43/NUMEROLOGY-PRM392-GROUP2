using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Numerology.Core.Constants;
using Numerology.Core.Interfaces;
using Numerology.Core.Models;
using Numerology.Core.Models.DTOs.Bets;

namespace Numerology.API.Controllers;

[Route("api/[controller]")]
[ApiController]

public class BetsController(IBetService betService) : ControllerBase
{
    private readonly IBetService _betService = betService;
    [HttpGet("expected-profit")]
    [Authorize]
    public async Task<ActionResult<BetsDTO>> GetExpectedProfit([FromQuery]decimal additionalCapital = 0)
    {
        try
        {
            var betsProfit = await _betService.CalculateExpectedProfit(additionalCapital);
            return Ok(new Response
            {
                Status = ResponseStatus.SUCCESS,
                Data = betsProfit,
                Message = "Expected profit calculated successfully."
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