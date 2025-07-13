using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace Numerology.Infrastructure.Migrations
{
    /// <inheritdoc />
    public partial class AddData : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterDatabase()
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "Roles",
                columns: table => new
                {
                    Id = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    Name = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    NormalizedName = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    ConcurrencyStamp = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Roles", x => x.Id);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "tblBets",
                columns: table => new
                {
                    Id = table.Column<Guid>(type: "char(36)", nullable: false, collation: "ascii_general_ci"),
                    Number = table.Column<int>(type: "int", nullable: false),
                    Amount = table.Column<decimal>(type: "decimal(65,30)", nullable: false),
                    NameUser = table.Column<string>(type: "longtext", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    Created = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    CreatedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci"),
                    LastModified = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    LastModifiedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tblBets", x => x.Id);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "tblLotteryResult",
                columns: table => new
                {
                    Id = table.Column<Guid>(type: "char(36)", nullable: false, collation: "ascii_general_ci"),
                    Date = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    Special = table.Column<int>(type: "int", nullable: false),
                    Prize1 = table.Column<int>(type: "int", nullable: false),
                    Prize2_1 = table.Column<int>(type: "int", nullable: false),
                    Prize2_2 = table.Column<int>(type: "int", nullable: false),
                    Prize3_1 = table.Column<int>(type: "int", nullable: false),
                    Prize3_2 = table.Column<int>(type: "int", nullable: false),
                    Prize3_3 = table.Column<int>(type: "int", nullable: false),
                    Prize3_4 = table.Column<int>(type: "int", nullable: false),
                    Prize3_5 = table.Column<int>(type: "int", nullable: false),
                    Prize3_6 = table.Column<int>(type: "int", nullable: false),
                    Prize4_1 = table.Column<int>(type: "int", nullable: false),
                    Prize4_2 = table.Column<int>(type: "int", nullable: false),
                    Prize4_3 = table.Column<int>(type: "int", nullable: false),
                    Prize4_4 = table.Column<int>(type: "int", nullable: false),
                    Prize5_1 = table.Column<int>(type: "int", nullable: false),
                    Prize5_2 = table.Column<int>(type: "int", nullable: false),
                    Prize5_3 = table.Column<int>(type: "int", nullable: false),
                    Prize5_4 = table.Column<int>(type: "int", nullable: false),
                    Prize5_5 = table.Column<int>(type: "int", nullable: false),
                    Prize5_6 = table.Column<int>(type: "int", nullable: false),
                    Prize6_1 = table.Column<int>(type: "int", nullable: false),
                    Prize6_2 = table.Column<int>(type: "int", nullable: false),
                    Prize6_3 = table.Column<int>(type: "int", nullable: false),
                    Prize7_1 = table.Column<int>(type: "int", nullable: false),
                    Prize7_2 = table.Column<int>(type: "int", nullable: false),
                    Prize7_3 = table.Column<int>(type: "int", nullable: false),
                    Prize7_4 = table.Column<int>(type: "int", nullable: false),
                    Created = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    CreatedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci"),
                    LastModified = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    LastModifiedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tblLotteryResult", x => x.Id);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateTable(
                name: "tblUsers",
                columns: table => new
                {
                    Id = table.Column<string>(type: "varchar(255)", nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    RefreshToken = table.Column<string>(type: "varchar(100)", maxLength: 100, nullable: false)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    RefreshTokenExpiryTime = table.Column<DateTime>(type: "datetime(6)", nullable: false),
                    RoleId = table.Column<string>(type: "varchar(255)", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    FullName = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    Created = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    CreatedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci"),
                    LastModified = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: false),
                    LastModifiedBy = table.Column<Guid>(type: "char(36)", nullable: true, collation: "ascii_general_ci"),
                    UserName = table.Column<string>(type: "varchar(256)", maxLength: 256, nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    NormalizedUserName = table.Column<string>(type: "varchar(256)", maxLength: 256, nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    Email = table.Column<string>(type: "varchar(256)", maxLength: 256, nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    NormalizedEmail = table.Column<string>(type: "varchar(256)", maxLength: 256, nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    EmailConfirmed = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    PasswordHash = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    SecurityStamp = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    ConcurrencyStamp = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    PhoneNumber = table.Column<string>(type: "longtext", nullable: true)
                        .Annotation("MySql:CharSet", "utf8mb4"),
                    PhoneNumberConfirmed = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    TwoFactorEnabled = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    LockoutEnd = table.Column<DateTimeOffset>(type: "datetime(6)", nullable: true),
                    LockoutEnabled = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    AccessFailedCount = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tblUsers", x => x.Id);
                    table.ForeignKey(
                        name: "FK_tblUsers_Roles_RoleId",
                        column: x => x.RoleId,
                        principalTable: "Roles",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.SetNull);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.InsertData(
                table: "Roles",
                columns: new[] { "Id", "ConcurrencyStamp", "Name", "NormalizedName" },
                values: new object[,]
                {
                    { "8cf89f78-6561-4a8e-b121-5c5e87c3913b", null, "SubAgent", "SUBAGENT" },
                    { "a84968cc-bda4-4633-a0ca-46e4ace734da", null, "GeneralAgent", "GENERALAGENT" }
                });

            migrationBuilder.CreateIndex(
                name: "IX_tblLotteryResult_Created",
                table: "tblLotteryResult",
                column: "Created",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "EmailIndex",
                table: "tblUsers",
                column: "NormalizedEmail");

            migrationBuilder.CreateIndex(
                name: "IX_tblUsers_RoleId",
                table: "tblUsers",
                column: "RoleId");

            migrationBuilder.CreateIndex(
                name: "UserNameIndex",
                table: "tblUsers",
                column: "NormalizedUserName",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tblBets");

            migrationBuilder.DropTable(
                name: "tblLotteryResult");

            migrationBuilder.DropTable(
                name: "tblUsers");

            migrationBuilder.DropTable(
                name: "Roles");
        }
    }
}
