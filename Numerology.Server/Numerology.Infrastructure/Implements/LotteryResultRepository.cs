//using Microsoft.EntityFrameworkCore;
//using Numerology.Core.Models.Entities;
//using Numerology.Core.Repositories;
//using Numerology.Infrastructure.Data;

//namespace Numerology.Infrastructure.Implements;

//public class LotteryResultRepository(ApplicationDBContext context) : ILotteryResultRepository
//{
//    private readonly ApplicationDBContext _context = context;
//    public async Task AddLotteryResultAsync(LotteryResult lotteryResult)
//    {
//        await _context.LotteryResults.AddAsync(lotteryResult);
//        await _context.SaveChangesAsync();
//    }
//    public async Task<bool> ExistingResult(DateTime date)
//    {
//        return await _context.LotteryResults.Where(r => r.Date.Date == date.Date)
//            .AnyAsync();
//    }   
//    public async Task<LotteryResult?> GetLotteryResultByDateAsync()
//    {
//        return await _context.LotteryResults
//            .FirstOrDefaultAsync(r => r.Date.Date == DateTime.Now.Date);
//    }
//}