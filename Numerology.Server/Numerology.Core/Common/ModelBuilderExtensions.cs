using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Numerology.Core.Constants;

namespace Numerology.Core.Common;

public static class ModelBuilderExtensions
{
    public static void SeedDatabase(this ModelBuilder builder)
    {
        var roles = UserRole.ALL.Select(role => new IdentityRole
        {
            Name = role,
            NormalizedName = role.ToUpper()
        }).ToList();
        builder.Entity<IdentityRole>().HasData(roles);
    }
}