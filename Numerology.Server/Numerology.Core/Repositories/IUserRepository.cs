using Numerology.Core.Models.Entities;

namespace Numerology.Core.Repositories;

public interface IUserRepository
{
   Task<User?> GetUserByUsernameAsync(string username);
}