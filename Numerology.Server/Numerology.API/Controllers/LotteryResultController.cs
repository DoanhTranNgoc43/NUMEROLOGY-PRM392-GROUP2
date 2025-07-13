using Microsoft.AspNetCore.Mvc;
using Numerology.Core.Interfaces;

namespace Numerology.API.Controllers;

[Route("api/[controller]")]
[ApiController]
public class LotteryResultController(ILotteryResultService lotteryResultService) : ControllerBase
{
    private readonly ILotteryResultService _lotteryResultService = lotteryResultService;

    [HttpPost("save-today-result")]
    public async Task<IActionResult> SaveTodayResult()
    {
        try
        {
            await _lotteryResultService.SaveTodayResultAsync();
            return Ok(new { Message = "Today's lottery result saved successfully." });
        }
        catch (Exception ex)
        {
            return BadRequest(new { Message = ex.Message });
        }
    }
    [HttpGet("get-today-result")]
    public async Task<IActionResult> GetTodayResult()
    {
        try
        {
            var result = await _lotteryResultService.GetTodayResultAsync();
            if (result == null)
            {
                return NotFound(new { Message = "No lottery result found for today." });
            }
            return Ok(result);
        }
        catch (Exception ex)
        {
            return BadRequest(new { Message = ex.Message });
        }
    }
}