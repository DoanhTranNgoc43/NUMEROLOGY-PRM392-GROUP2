using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Numerology.Core.Models.Entities;

namespace Numerology.Infrastructure.Data;

public class ApplicationDBContext(DbContextOptions<ApplicationDBContext> options) : IdentityDbContext<User>(options)
{
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
        builder.Ignore<IdentityUserClaim<string>>();
        builder.Ignore<IdentityUserLogin<string>>();
        builder.Ignore<IdentityUserToken<string>>();
        builder.Ignore<IdentityUserRole<string>>();
        builder.Ignore<IdentityRoleClaim<string>>();
        builder.Entity<User>().ToTable("tblUsers");
        builder.Entity<IdentityRole>().ToTable("tblRoles");
        builder.Entity<User>()
            .HasOne(u => u.Role)
            .WithMany()
            .HasForeignKey(u => u.RoleId)
            .OnDelete(DeleteBehavior.SetNull);
        builder.Ignore<IdentityUserRole<string>>();
    }
}