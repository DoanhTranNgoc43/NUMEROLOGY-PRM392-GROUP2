using Numerology.Core.Models.DTOs.LotteryResult;

namespace Numerology.Core.Interfaces;

public interface ILotteryResultService
{
    Task SaveTodayResultAsync();
    Task<LotteryResultDTO?> GetTodayResultAsync();
}