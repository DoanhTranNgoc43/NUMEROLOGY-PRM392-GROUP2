namespace Numerology.Core.Models.DTOs.Bets;

public class BetsDTO
{
    public int Number { get; set; }
    public decimal Amount { get; set; }
    public string Region { get; set; } = string.Empty;
    public string RiskLevel { get; set; } = string.Empty;
    public int TicketCount { get; set; }
    public required string UserId { get; set; }
}