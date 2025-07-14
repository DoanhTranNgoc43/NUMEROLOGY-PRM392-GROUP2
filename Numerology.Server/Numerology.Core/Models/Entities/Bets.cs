namespace Numerology.Core.Models.Entities;

public class Bets : BaseEntity
{
    public int Number { get; set; }
    public decimal Amount { get; set; }
    public string NameUser { get; set; } = string.Empty;
    public User User { get; set; }
}