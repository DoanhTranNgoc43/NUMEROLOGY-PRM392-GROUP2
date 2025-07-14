using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Numerology.API.Mappers;
using Numerology.API.ServiceExtension;
using Numerology.Core.Constants;
using Numerology.Core.Models;
using Numerology.Core.Models.Configs;
using Numerology.Core.Models.Entities;
using Numerology.Core.Repositories;
using Numerology.Infrastructure.Data;
using System.Text;

namespace Numerology.API
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);



            builder.Services.AddControllers()
             .ConfigureApiBehaviorOptions(options =>
             {
                 options.InvalidModelStateResponseFactory = context =>
                 {
                     var errors = context.ModelState.Values
                     .SelectMany(v => v.Errors)
                     .Select(e => e.ErrorMessage)
                     .FirstOrDefault();

                     var response = new Response
                     {
                         Status = ResponseStatus.ERROR,
                         Message = errors ?? "Invalid request"
                     };

                     return new BadRequestObjectResult(response);
                 };
             });
            var jwtSection = builder.Configuration.GetSection("JWT");
            builder.Services.Configure<JwtConfig>(jwtSection);
            var jwtConfig = jwtSection.Get<JwtConfig>()
                 ?? throw new Exception("Jwt options have not been set!");
            builder.Services.AddDbContext<ApplicationDBContext>(options =>
            {
                options.UseSqlServer(
                    builder.Configuration.GetConnectionString("DefaultConnection"),
                    sqlOptions => sqlOptions.EnableRetryOnFailure()
                );
            });

            builder.Services.AddCors(options =>
            {
                options.AddPolicy(Policy.SINGLE_PAGE_APP, policy =>
                {
                    policy.WithOrigins(jwtConfig.Audience);
                    policy.AllowAnyHeader();
                    policy.AllowAnyMethod();
                    policy.AllowCredentials();
                });
            }); 
           


            builder.Services.AddControllers();
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "Numerology.API", Version = "v1" });
                c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
                {
                    Description = "JWT Authorization header using the Bearer scheme. Example: 'Bearer {your token}'",
                    Name = "Authorization",
                    In = ParameterLocation.Header,
                    Type = SecuritySchemeType.Http,
                    Scheme = "bearer",
                    BearerFormat = "JWT"
                });

                c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            Array.Empty<string>()
        }
    });
            });
            builder.Services.AddAutoMapper(typeof(MapperProfile));
            builder.Services.AddIdentity<User, IdentityRole>(options =>
            {
                options.Password.RequiredLength = 8;
                options.Password.RequireUppercase =
                options.Password.RequireLowercase =
                options.Password.RequireNonAlphanumeric = false;
                options.User.RequireUniqueEmail = true;
                options.SignIn.RequireConfirmedEmail = true;
                options.Tokens.PasswordResetTokenProvider = TokenOptions.DefaultProvider;
                options.Tokens.EmailConfirmationTokenProvider = TokenOptions.DefaultProvider;
                options.Tokens.AuthenticatorTokenProvider = TokenOptions.DefaultProvider;
            }
            ).AddEntityFrameworkStores<ApplicationDBContext>()
            .AddDefaultTokenProviders();
            builder.Services
         .AddAuthentication(options =>
         {
             options.DefaultAuthenticateScheme =
                 options.DefaultChallengeScheme =
                     options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
         })
        .AddJwtBearer(option =>
        {
            option.TokenValidationParameters = new()
            {
                ValidateIssuerSigningKey = true,
                ClockSkew = TimeSpan.Zero,
                ValidIssuer = jwtConfig.Issuer,
                ValidAudience = jwtConfig.Audience,
                IssuerSigningKey = new SymmetricSecurityKey(
                    Encoding.UTF8.GetBytes(jwtConfig.SigningKey)
                )
            };
            option.Events = new()
            {
                OnMessageReceived = context =>
                {
                    context.Request.Cookies.TryGetValue(jwtConfig.AccessTokenKey, out var accessToken);
                    if (!string.IsNullOrEmpty(accessToken))
                        context.Token = accessToken;
                    return Task.CompletedTask;
                }
            };
        });
            builder.Services.RegisterService();
            var app = builder.Build();
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }
            app.UseCors(Policy.SINGLE_PAGE_APP);
            app.UseHttpsRedirection();
            app.UseAuthentication();
            app.UseAuthorization();
            app.MapControllers();
            app.Run();
        }

    }
}
