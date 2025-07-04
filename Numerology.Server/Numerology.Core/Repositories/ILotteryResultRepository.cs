using Numerology.Core.Models.Entities;

namespace Numerology.Core.Repositories;

public interface ILotteryResultRepository
{
    Task AddLotteryResultAsync(LotteryResult lotteryResult);
    Task<bool> ExistingResult(DateTime date);
    Task<LotteryResult?> GetLotteryResultByDateAsync();
}