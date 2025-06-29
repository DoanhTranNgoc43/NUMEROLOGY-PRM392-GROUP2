using Numerology.Core.Models.Entities;

namespace Numerology.Core.Repositories;

public interface IBetRepository
{
    Task AddBet(Bets bet);
    Task<decimal> TotalAmount();
    Task<int> GetBetCount();
    Task<decimal> GetAmountMax();
    Task<(int Number, decimal Amount)?> GetNumberRickBet();
    Task<decimal> TotalAmountNumberRisk();
    Task<bool> IsAllBetsWithAmount(decimal amount);
}