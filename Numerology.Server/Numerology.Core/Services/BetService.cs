using Numerology.Core.Constants;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.DTOs.Bets;
using Numerology.Core.Repositories;

namespace Numerology.Core.Services;

public class BetService(IBetRepository betRepository) : IBetService
{
    private readonly IBetRepository _betRepository = betRepository;

    public async Task<BetsProfitDTO?> CalculateExpectedProfit(decimal additionalCapital = 0)
    {
        BetsProfitDTO? result = null;
        var totalAmount = await _betRepository.TotalAmount();
        if (totalAmount <= 0)
        {
            return null; 
        }
        decimal availableCapital = totalAmount + additionalCapital;
        int countNumber = await _betRepository.GetBetCount();
        var maxBetAmount = await _betRepository.GetNumberRickBet();
        if (countNumber >= Bets.BETS_WIN && await _betRepository.IsAllBetsWithAmount(maxBetAmount.Value.Amount)
        && additionalCapital == 0)
        {
            var payoutCost = 0.01m * 70 * availableCapital;
            var expectedProfit = availableCapital - payoutCost;
            var profitPercentage = expectedProfit / availableCapital * 100;
            if (profitPercentage >= Bets.PROFIT_THRESHOLD_PERCENT)
            {
                result = new BetsProfitDTO
                {
                    TotalRevenue = totalAmount,
                    AdditionalCapital = additionalCapital,
                    AvailableCapital = availableCapital,
                    MaxBetAmount = maxBetAmount.Value.Amount,
                    ExpectedProfit = expectedProfit,
                    NumberCount = countNumber,
                    MaxBetNumber = maxBetAmount.Value.Number,
                    CoveragePercent = 100m,
                    RiskLevel = "Nên Ôm",
                    Reason = $"Tất cả số đều có tiền cược {maxBetAmount.Value.Amount:N0}, " +
                             $"vốn khả dụng {availableCapital:N0} đủ để trả tiền cược",
                    ShouldAccept = true,
                    MaxPayout = payoutCost
                };
            }
        }
        else if (additionalCapital == 0 && !await _betRepository.IsAllBetsWithAmount(maxBetAmount.Value.Amount))
        {
            decimal numberCoverage = countNumber / Bets.TOTAL_NUMBERS * Bets.PERCENT_MULTIPLIER;
            var maxPayout = maxBetAmount.Value.Amount * Bets.BETS_WIN;
            result = new BetsProfitDTO
            {
                TotalRevenue = totalAmount,
                AdditionalCapital = additionalCapital,
                AvailableCapital = availableCapital,
                MaxBetAmount = maxBetAmount.Value.Amount,
                MaxBetNumber = maxBetAmount.Value.Number,
                MaxPayout = maxPayout,
                ExpectedProfit = totalAmount - maxPayout,
                NumberCount = countNumber,
                CoveragePercent = numberCoverage
            };
            DetermineRiskLevel(result!);
        }
        return result;
    }

    // public async Task<BetsProfitDTO?> CalculateExpectedProfit(decimal capital = 0)
    // {
    //     // 1. Lấy dữ liệu cơ bản
    //     var totalAmount = await _betRepository.TotalAmount();        // Tổng tiền thu từ khách
    //     var maxBetAmount = await _betRepository.GetAmountMax();      // Số có tiền cược cao nhất
    //     var betCount = await _betRepository.GetBetCount();           // Số lượng số được đặt

    //     if (totalAmount <= 0 || betCount <= 0)
    //     {
    //         return null;
    //     }

    //     // 2. Tính vốn khả dụng = Tiền thu + Vốn bổ sung
    //     decimal availableCapital = totalAmount + capital;

    //     // 3. Tính tiền phải trả tối đa
    //     decimal maxPayout = maxBetAmount * Bets.BETS_WIN;

    //     // 4. Tính độ phủ số
    //     decimal coveragePercent = betCount / Bets.TOTAL_NUMBERS * 100m;

    //     // 5. Tính lãi/lỗ dự kiến (nếu về số nóng nhất)
    //     decimal expectedProfit = availableCapital - maxPayout;

    //     // 6. Quyết định: So sánh tiền phải trả với vốn khả dụng
    //     bool shouldAccept = maxPayout <= availableCapital;

    //     var result = new BetsProfitDTO
    //     {
    //         TotalRevenue = totalAmount,           // Tiền thu từ khách
    //         AdditionalCapital = capital,          // Vốn bổ sung
    //         AvailableCapital = availableCapital,
    //         MaxBetAmount = maxBetAmount,
    //         MaxPayout = maxPayout,
    //         ExpectedProfit = expectedProfit,
    //         NumberCount = betCount,
    //         CoveragePercent = coveragePercent,
    //         ShouldAccept = shouldAccept
    //     };
    //     DetermineRiskLevel(result);

    //     return result;

    // }
    private void DetermineRiskLevel(BetsProfitDTO result)
    {
        // Ưu tiên kiểm tra vốn
        if (result.MaxPayout > result.AvailableCapital)
        {
            result.RiskLevel = "Không Nên Ôm";
            result.Reason = $"Thiếu vốn: Phải trả {result.MaxPayout:N0} > Vốn có {result.AvailableCapital:N0}";
            result.ShouldAccept = false;
            return;
        }
        // Kiểm tra độ phủ số
        if (result.CoveragePercent >= 80)
        {
            result.RiskLevel = "Nên Ôm";
            result.Reason = $"An toàn: Đủ vốn + Độ phủ cao ({result.CoveragePercent:F1}%)";
        }
        else if (result.CoveragePercent >= 60)
        {
            result.RiskLevel = "Có Thể Ôm";
            result.Reason = $"Chấp nhận được: Đủ vốn + Độ phủ {result.CoveragePercent:F1}%";
        }
        else if (result.CoveragePercent >= 40)
        {
            result.RiskLevel = "Cần Kiểm Tra";
            result.Reason = $"Cẩn thận: Đủ vốn nhưng độ phủ thấp ({result.CoveragePercent:F1}%)";
        }
        else
        {
            result.RiskLevel = "Không Nên Ôm";
            result.Reason = $"Nguy hiểm: Độ phủ quá thấp ({result.CoveragePercent:F1}%)";
            result.ShouldAccept = false;
        }
    }
}