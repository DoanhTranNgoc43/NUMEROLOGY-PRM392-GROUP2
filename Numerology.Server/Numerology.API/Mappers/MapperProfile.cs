using Numerology.Core.Models.DTOs.Auth.User;
using Numerology.Core.Models.Entities;

namespace Numerology.API.Mappers;

public class MapperProfile : AutoMapper.Profile
{
    public MapperProfile()
    {
        CreateMap<User, UserDTO>().ReverseMap();
    }
}