using Numerology.Core.Models.DTOs.LotteryResult;
using Numerology.Core.Models.Entities;

namespace Numerology.API.Mappers;

public class MapperProfile : AutoMapper.Profile
{
    public MapperProfile()
    {
        CreateMap<LotteryResult, LotteryResultDTO>().ReverseMap();
    }
}