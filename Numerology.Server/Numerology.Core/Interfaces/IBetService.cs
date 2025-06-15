using Numerology.Core.Models.DTOs.Bets;

namespace Numerology.Core.Interfaces;

public interface IBetService
{
    Task<BetsProfitDTO?> CalculateExpectedProfit(decimal capital = 0);
}