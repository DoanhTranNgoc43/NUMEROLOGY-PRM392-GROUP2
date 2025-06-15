using Microsoft.EntityFrameworkCore;
using Numerology.Core.Models.Entities;
using Numerology.Core.Repositories;
using Numerology.Infrastructure.Data;

namespace Numerology.Infrastructure.Implements;

public class BetRepository(ApplicationDBContext context) : IBetRepository
{
    private readonly ApplicationDBContext _context = context;
    public async Task AddBet(Bets bet)
    {
        await _context.Bets.AddAsync(bet);
        await _context.SaveChangesAsync();
    }
    public async Task<decimal> TotalAmount()
    {
        return await _context.Bets
        .Where(b => b.Created.Date == DateTime.Today.Date)
        .SumAsync(b => b.Amount);
    }
    public async Task<decimal> GetFirstAmount()
    {
        return await _context.Bets
        .Where(b => b.Created.Date == DateTime.Today.Date)
        .Select(b => b.Amount)
        .FirstOrDefaultAsync();
    }
    public async Task<int> GetBetCount()
    {
        return await _context.Bets
        .Where(b => b.Created.Date == DateTime.Today.Date)
        .CountAsync();
    }
    public async Task<decimal> GetAmountMax()
    {
        return await _context.Bets
        .Where(b => b.Created.Date == DateTime.Today.Date)
        .MaxAsync(b => b.Amount);
    }
    public async Task<(int Number, decimal Amount)?> GetNumberRickBet()
    {
        var result = await _context.Bets
            .Where(b => b.Created.Date == DateTime.Today.Date)
            .GroupBy(b => b.Number)
            .Select(g => new { Number = g.Key, TotalAmount = g.Sum(b => b.Amount) })
            .OrderByDescending(g => g.TotalAmount)
            .FirstOrDefaultAsync();

        return result != null ? (result.Number, result.TotalAmount) : null;
    }
    public async Task<decimal> TotalAmountNumberRisk()
    {
        return await _context.Bets
        .Where(b => b.Created.Date == DateTime.Today.Date)
        .GroupBy(b => b.Number)
        .Select(g => new { Number = g.Key, TotalAmount = g.Sum(b => b.Amount) })
        .SumAsync(g => g.TotalAmount);
    }
}