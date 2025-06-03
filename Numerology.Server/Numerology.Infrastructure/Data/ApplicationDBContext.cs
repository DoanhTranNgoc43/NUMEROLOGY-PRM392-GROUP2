using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Numerology.Core.Common;
using Numerology.Core.Models.Entities;

namespace Numerology.Infrastructure.Data;

public class ApplicationDBContext(DbContextOptions<ApplicationDBContext> options) : IdentityDbContext<User>(options)
{
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
        builder.Entity<IdentityRole>().ToTable("tblRoles");
        builder.Entity<IdentityRole>().ToTable("tblRoles");
        builder.Entity<IdentityRoleClaim<string>>().ToTable("tblRoleClaims");
        builder.Entity<IdentityUserClaim<string>>().ToTable("tblUserClaims");
        builder.Entity<IdentityUserRole<string>>().ToTable("tblUserRoles");
        builder.Entity<IdentityUserToken<string>>().ToTable("tblUserTokens");
        builder.Entity<IdentityUserLogin<string>>().ToTable("tblUserLogins");
        builder.Entity<User>().ToTable("tblUsers");
        builder.Entity<User>()
            .HasOne(u => u.Role)
            .WithMany()
            .HasForeignKey(u => u.RoleId)
            .OnDelete(DeleteBehavior.SetNull);
        builder.SeedDatabase();
    }
}