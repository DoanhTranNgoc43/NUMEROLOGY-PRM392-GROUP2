namespace Numerology.Core.Models.DTOs.User;

public class UserDTO
{
    public string Username { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Avatar { get; set; } = string.Empty;
    public string Banner { get; set; } = string.Empty;
    public DateTimeOffset? LockoutEnd { get; set; } = null;
    public string? Role { get; set; } = null;
}