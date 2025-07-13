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
<<<<<<< HEAD


        builder.Entity<IdentityRole>().ToTable("tblRoles");
        builder.Entity<IdentityRoleClaim<string>>().ToTable("tblRoleClaims");
        builder.Entity<IdentityUserClaim<string>>().ToTable("tblUserClaims");
        builder.Entity<IdentityUserRole<string>>().ToTable("tblUserRoles");
        builder.Entity<IdentityUserToken<string>>().ToTable("tblUserTokens");
        builder.Entity<IdentityUserLogin<string>>().ToTable("tblUserLogins");
=======
        builder.Ignore<IdentityRole>();
        builder.Ignore<IdentityRoleClaim<string>>();
        builder.Ignore<IdentityUserClaim<string>>();
        builder.Ignore<IdentityUserRole<string>>();
        builder.Ignore<IdentityUserToken<string>>();
        builder.Ignore<IdentityUserLogin<string>>();
>>>>>>> 8615bf1956a40d74f1d3d179c17f00837dbcba1f
        builder.Entity<User>().ToTable("tblUsers");

        builder.Entity<User>()
            .HasOne(u => u.Role)
            .WithMany()
            .HasForeignKey(u => u.RoleId)
            .OnDelete(DeleteBehavior.SetNull);

    
        builder.Entity<Bets>().ToTable("tblBets");
<<<<<<< HEAD
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
=======
        builder.Entity<LotteryResult>().ToTable("tblLotteryResult")
                .HasIndex(l => l.Created)
                .IsUnique();
        builder.SeedDatabase();
    }
    public DbSet<Bets> Bets { get; set; }
    public DbSet<LotteryResult> LotteryResults { get; set; }
>>>>>>> 8615bf1956a40d74f1d3d179c17f00837dbcba1f
}