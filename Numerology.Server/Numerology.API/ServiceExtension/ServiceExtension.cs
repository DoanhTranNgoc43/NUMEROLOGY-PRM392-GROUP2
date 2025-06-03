using Numerology.Core.Interfaces;
using Numerology.Core.Services;

namespace Numerology.API.ServiceExtension;

public static class ServiceExtension
{
    public static IServiceCollection RegisterService(this IServiceCollection services)
    {
        #region Services
        services.AddScoped<IIdentityService, IdentityService>();
        #endregion
        #region Repositories
        #endregion
        return services;
    }
}