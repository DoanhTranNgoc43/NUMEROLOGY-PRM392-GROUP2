using System.Security.Claims;
using Google.Apis.Auth;
using Numerology.Core.Models.DTOs.Auth;

namespace Numerology.Core.Interfaces;

public interface ITokenService
{
    Task<GoogleJsonWebSignature.Payload?> VerifyGoogleToken(ExternalAuthDTO externalAuth);
    string GenerateRefreshToken();
    string GenerateAccessToken(List<Claim> claims);
}