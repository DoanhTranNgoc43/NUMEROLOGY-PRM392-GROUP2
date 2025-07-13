using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Numerology.Core.Models.DTOs.Auth
{
    public class UpdateProfileDTO
    {
        [StringLength(100, ErrorMessage = "Full name cannot exceed 100 characters")]
        public string? FullName { get; set; }

        [EmailAddress(ErrorMessage = "Invalid email format")]
        [StringLength(256, ErrorMessage = "Email cannot exceed 256 characters")]
        public string? Email { get; set; }

        [Phone(ErrorMessage = "Invalid phone number format")]
        [StringLength(15, ErrorMessage = "Phone number cannot exceed 15 characters")]
        public string? PhoneNumber { get; set; }
    }
}
