namespace Numerology.Core.Models.DTOs.Bets;

public class BetsProfitDTO
{
     // Tài chính
    public decimal TotalRevenue { get; set; }        // Tiền thu từ khách
    public decimal AdditionalCapital { get; set; }   // Vốn bổ sung (input từ user)
    public decimal AvailableCapital { get; set; }    // = TotalRevenue + AdditionalCapital
    
    // Rủi ro
    public decimal MaxBetAmount { get; set; }        // Số có tiền cược cao nhất
    public int? MaxBetNumber { get; set; }            // Số nào nguy hiểm nhất
    public decimal MaxPayout { get; set; }           // Tiền phải trả tối đa
    public decimal ExpectedProfit { get; set; }      // Lãi/lỗ nếu về số nóng
    
    // Độ phủ số
    public int NumberCount { get; set; }             // Số lượng số được đặt
    public decimal CoveragePercent { get; set; }     // % độ phủ số
    
    // Kết luận
    public bool ShouldAccept { get; set; }
    public string RiskLevel { get; set; } = string.Empty;
    public string Reason { get; set; } = string.Empty;
}