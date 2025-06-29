using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace Numerology.Core.Models.Entities;

public class User : IdentityUser
{
    [MaxLength(100)]
    public string RefreshToken { get; set; } = string.Empty;
    public DateTime RefreshTokenExpiryTime { get; set; }
    public string? RoleId { get; set; }
    public virtual IdentityRole? Role { get; set; }
    public string? FullName { get; set; }
    public DateTimeOffset Created { get; set; }
    public Guid? CreatedBy { get; set; }
    public DateTimeOffset LastModified { get; set; }
    public Guid? LastModifiedBy { get; set; }
}