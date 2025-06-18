namespace Numerology.Core.Models.Entities;

public class Bets : BaseEntity
{
    public int Number { get; set; }
    public decimal Amount { get; set; }
    public string Region { get; set; } = string.Empty;
    public string RiskLevel { get; set; } = string.Empty;
    public int TicketCount { get; set; }
    public required virtual User User { get; set; }
}