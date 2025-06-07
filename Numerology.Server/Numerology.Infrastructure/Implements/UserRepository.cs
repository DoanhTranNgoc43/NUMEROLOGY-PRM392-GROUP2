using Microsoft.EntityFrameworkCore;
using Numerology.Core.Models.Entities;
using Numerology.Core.Repositories;
using Numerology.Infrastructure.Data;

namespace Numerology.Infrastructure.Implements;

public class UserRepository(ApplicationDBContext context) : IUserRepository
{
    private readonly ApplicationDBContext _context = context;
    public async Task<User?> GetUserByUsernameAsync(string username)
    {
        return await _context.Users
        .Include(u => u.Role)
        .Where(u => u.UserName == username)
        .FirstOrDefaultAsync();
    }
}