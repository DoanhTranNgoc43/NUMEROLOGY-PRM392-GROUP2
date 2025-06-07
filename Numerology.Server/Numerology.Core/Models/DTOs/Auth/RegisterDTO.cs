using System.ComponentModel.DataAnnotations;

namespace Numerology.Core.Models.DTOs.Auth;
public class RegisterDTO
{
    [Required(ErrorMessage = "Email cannot be empty")]
    [StringLength(256, ErrorMessage = "Email cannot exceed 256 characters")]
    [EmailAddress(ErrorMessage = "Invalid email format")]
    public string Email { get; set; } = string.Empty;
    [Required(ErrorMessage = "Username cannot be empty")]
    [StringLength(50, MinimumLength = 3, ErrorMessage = "Username must be between 3 and 50 characters")]
    [RegularExpression(@"^[a-zA-Z0-9_]+$", ErrorMessage = "Username can only contain letters, numbers, and underscores")]
    public string Username { get; set; } = string.Empty;
    [Required(ErrorMessage = "Password cannot be empty")]
    [StringLength(100, MinimumLength = 6,
    ErrorMessage = "Password must be between 6 and 100 characters")]
    [RegularExpression(@"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?\"":{}|<>]).{6,}$",
    ErrorMessage = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character")]
    public string Password { get; set; } = string.Empty;
}