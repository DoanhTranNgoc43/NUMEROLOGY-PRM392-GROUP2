using Numerology.Core.Interfaces;
using Numerology.Core.Repositories;
using Numerology.Core.Services;
using Numerology.Infrastructure.Implements;

namespace Numerology.API.ServiceExtension;

public static class ServiceExtension
{
    public static IServiceCollection RegisterService(this IServiceCollection services)
    {
        #region Services
        services.AddScoped<IIdentityService, IdentityService>();
        services.AddScoped<IAuthService, AuthService>();
        #endregion
        
        #region Repositories
        services.AddScoped<IUserRepository, UserRepository>();
        #endregion
        return services;
    }
}