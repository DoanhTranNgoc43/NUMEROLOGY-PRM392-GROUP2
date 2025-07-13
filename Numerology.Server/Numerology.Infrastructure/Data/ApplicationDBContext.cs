using System.Security.Cryptography.X509Certificates;
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


        builder.Entity<Bets>().ToTable("tblBets");
        builder.Entity<Bets>()
           .HasOne(b => b.User)
           .WithMany(u => u.Bets)
      .OnDelete(DeleteBehavior.Cascade);


        builder.Entity<Bets>()
            .Property(b => b.Amount)
            .HasColumnType("decimal(18,2)");


        builder.SeedDatabase();
    }


    public DbSet<Bets> Bets { get; set; }
}