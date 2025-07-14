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
        var isAllBetsWithAmount = await _betRepository.IsAllBetsWithAmount(maxBetAmount.Value.Amount);
        if (countNumber >= Bets.BETS_WIN && isAllBetsWithAmount
        && additionalCapital == 0)
        {
            var payoutCost = Bets.PAYOUT_RATE * Bets.BETS_WIN * availableCapital;
            var expectedProfit = availableCapital - payoutCost;
            var profitPercentage = expectedProfit / availableCapital * Bets.PERCENT_MULTIPLIER;
            if (profitPercentage >= Bets.PROFIT_THRESHOLD_PERCENT)
            {
                return CreateBetResult(totalAmount, additionalCapital, availableCapital,
                    maxBetAmount.Value.Amount, null, payoutCost, expectedProfit,
                    countNumber, Bets.FULL_COVERAGE_PERCENT, "Nên Ôm",
                    $"Tất cả số đều có tiền cược {maxBetAmount.Value.Amount:N0}, vốn khả dụng {availableCapital:N0} đủ để trả tiền cược",
                    true);
            }
        }
        if (additionalCapital == 0 && !isAllBetsWithAmount)
        {
            decimal numberCoverage = countNumber / Bets.TOTAL_NUMBERS * Bets.PERCENT_MULTIPLIER;

            //var maxPayout = maxBetAmount.Value.Amount * Bets.BETS_WIN;

            var maxPayout = maxBetAmount.Value.Amount * Bets.BETS_WIN * 0.01m;

            result = CreateBetResult(totalAmount, additionalCapital, availableCapital,
                maxBetAmount.Value.Amount, maxBetAmount.Value.Number, maxPayout,
                totalAmount - maxPayout, countNumber, numberCoverage);
            DetermineRiskLevel(result);
            return result;
        }
        if (additionalCapital > 0 && !isAllBetsWithAmount)
        {
            decimal maxPayout = maxBetAmount.Value.Amount * Bets.BETS_WIN;
            decimal expectedProfit = availableCapital - maxPayout;
            if (expectedProfit >= 0)
            {
                return CreateBetResult(totalAmount, additionalCapital, availableCapital,
                    maxBetAmount.Value.Amount, maxBetAmount.Value.Number, maxPayout,
                    expectedProfit, countNumber, Bets.FULL_COVERAGE_PERCENT, "Nên Ôm",
                    $"Vốn bổ sung {additionalCapital:N0} + Tiền thu {totalAmount:N0} đủ để trả tiền cược",
                    true);
            }
            else
            {
                return CreateBetResult(totalAmount, additionalCapital, availableCapital,
                       maxBetAmount.Value.Amount, maxBetAmount.Value.Number, maxPayout,
                       expectedProfit, countNumber, Bets.FULL_COVERAGE_PERCENT, "Không Nên Ôm",
                       $"Vốn bổ sung {additionalCapital:N0} + Tiền thu {totalAmount:N0} không đủ để trả tiền cược",
                       false);
            }
        }
        return result;
    }



    private static BetsProfitDTO CreateBetResult(decimal totalRevenue, decimal additionalCapital,

        decimal availableCapital, decimal maxBetAmount, int? maxBetNumber, decimal maxPayout,
        decimal expectedProfit, int numberCount, decimal coveragePercent,
        string? riskLevel = null, string? reason = null, bool? shouldAccept = null)
    {
        return new BetsProfitDTO
        {
            TotalRevenue = totalRevenue,
            AdditionalCapital = additionalCapital,
            AvailableCapital = availableCapital,
            MaxBetAmount = maxBetAmount,
            MaxBetNumber = maxBetNumber,
            MaxPayout = maxPayout,
            ExpectedProfit = expectedProfit,
            NumberCount = numberCount,
            CoveragePercent = coveragePercent,
            RiskLevel = riskLevel!,
            Reason = reason!,
            ShouldAccept = shouldAccept ?? true
        };
    }

    private void DetermineRiskLevel(BetsProfitDTO result)


    {
        if (result.MaxPayout > result.AvailableCapital)
        {
            SetRisk(result, "Không Nên Ôm",
                $"Thiếu vốn: Phải trả {result.MaxPayout:N0} > Vốn có {result.AvailableCapital:N0}", false);
            return;
        }
        var (risk, reason, accept) = result.CoveragePercent switch
        {
            >= Bets.HIGH_COVERAGE_THRESHOLD => ("Nên Ôm", $"An toàn: Đủ vốn + Độ phủ cao ({result.CoveragePercent:F1}%)", true),
            >= Bets.MEDIUM_COVERAGE_THRESHOLD => ("Có Thể Ôm", $"Chấp nhận được: Đủ vốn + Độ phủ {result.CoveragePercent:F1}%", true),
            >= Bets.LOW_COVERAGE_THRESHOLD => ("Cần Kiểm Tra", $"Cẩn thận: Đủ vốn nhưng độ phủ thấp ({result.CoveragePercent:F1}%)", true),
            _ => ("Không Nên Ôm", $"Nguy hiểm: Độ phủ quá thấp ({result.CoveragePercent:F1}%)", false)
        };

        SetRisk(result, risk, reason, accept);
    }


 

    private static void SetRisk(BetsProfitDTO result, string riskLevel, string reason, bool shouldAccept)

    {
        result.RiskLevel = riskLevel;
        result.Reason = reason;
        result.ShouldAccept = shouldAccept;
    }
}