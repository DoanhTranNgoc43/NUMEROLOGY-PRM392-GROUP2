namespace Numerology.Core.Models.Entities;

public class BaseEntity 
{
    public Guid Id { get; set; } 
    public DateTimeOffset Created { get; set; }
    public Guid? CreatedBy { get; set; }
    public DateTimeOffset LastModified { get; set; }
    public Guid? LastModifiedBy { get; set; }
}