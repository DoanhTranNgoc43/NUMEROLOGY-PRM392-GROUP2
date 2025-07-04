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
        builder.Ignore<IdentityRole>();
        builder.Ignore<IdentityRoleClaim<string>>();
        builder.Ignore<IdentityUserClaim<string>>();
        builder.Ignore<IdentityUserRole<string>>();
        builder.Ignore<IdentityUserToken<string>>();
        builder.Ignore<IdentityUserLogin<string>>();
        builder.Entity<User>().ToTable("tblUsers");

        builder.Entity<User>()
            .HasOne(u => u.Role)
            .WithMany()
            .HasForeignKey(u => u.RoleId)
            .OnDelete(DeleteBehavior.SetNull);

        builder.Entity<Bets>().ToTable("tblBets");
        builder.Entity<LotteryResult>().ToTable("tblLotteryResult")
                .HasIndex(l => l.Created)
                .IsUnique();
        builder.SeedDatabase();
    }
    public DbSet<Bets> Bets { get; set; }
    public DbSet<LotteryResult> LotteryResults { get; set; }
}