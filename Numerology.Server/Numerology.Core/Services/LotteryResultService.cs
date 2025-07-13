using System.Text.Json;
using AutoMapper;
using Numerology.Core.Constants;
using Numerology.Core.Interfaces;
using Numerology.Core.Models.DTOs.LotteryResult;
using Numerology.Core.Repositories;

namespace Numerology.Core.Services;

public class LotteryResultService(ILotteryResultRepository lotteryResultRepository, HttpClient httpClient,
    IMapper mapper
) : ILotteryResultService
{
    private readonly ILotteryResultRepository _lotteryResultRepository = lotteryResultRepository;
    private readonly HttpClient _httpClient = httpClient;
    private readonly IMapper _mapper = mapper;
    private async Task<LotteryResultDTO[]> FetchLotteryResultsAsync()
    {
        try
        {
            var response = await _httpClient.GetStringAsync(LotteryResult.LOTTERY_RESULT_URL_API);
            var results = JsonSerializer.Deserialize<LotteryResultDTO[]>(response, new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            });
            return results!;
        }
        catch (Exception ex)
        {
            throw new Exception($"Error fetching lottery data: {ex.Message}");
        }
    }
    public async Task SaveTodayResultAsync()
    {
        var result = await FetchLotteryResultsAsync();
        if (result == null || result.Length == 0)
        {
            throw new Exception("No lottery results found for today.");
        }
        var lotteryResult = result.Where(r => r.Date.Date == DateTime.Now.Date).FirstOrDefault()
            ?? throw new Exception("No lottery results found for today.");
        var isExistingResult = await _lotteryResultRepository.ExistingResult(lotteryResult.Date);
        if (isExistingResult)
        {
            throw new Exception("Lottery result for today already exists.");
        }
        var entity = _mapper.Map<Models.Entities.LotteryResult>(lotteryResult);
        await _lotteryResultRepository.AddLotteryResultAsync(entity);
    }
    public async Task<LotteryResultDTO?> GetTodayResultAsync()
    {
        var result = await _lotteryResultRepository.GetLotteryResultByDateAsync();
        if (result == null)
        {
            return null;
        }
        return _mapper.Map<LotteryResultDTO>(result);
    }
}