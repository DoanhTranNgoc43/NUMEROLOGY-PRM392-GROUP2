using Hangfire;
using Numerology.Core.Interfaces;

namespace Numerology.API.ServiceExtension;

public static class RecurringJobsExtension
{
    [Obsolete]
    public static void ConfigureRecurringJobs(IRecurringJobManager recurringJobManager)
    {
        var vietnamTimeZone = TimeZoneInfo.FindSystemTimeZoneById("SE Asia Standard Time");
        var currentVietnamTime = TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow, vietnamTimeZone);
        recurringJobManager.AddOrUpdate<ILotteryResultService>(
            recurringJobId: "test-job-2min",
            methodCall: service => service.SaveTodayResultAsync(),
            cronExpression: "*/2 * * * *", // Mỗi 2 phút
            timeZone: vietnamTimeZone,
            queue: "default");        // Job 2: Lottery job (18:35 VN time)
        recurringJobManager.AddOrUpdate<ILotteryResultService>(
            recurringJobId: "daily-lottery-save",
            methodCall: service => service.SaveTodayResultAsync(),
            cronExpression: "35 18 * * *", // 18:35 mỗi ngày
            timeZone: vietnamTimeZone,
            queue: "critical");

        Console.WriteLine("✅ Jobs configured successfully!");
        Console.WriteLine("📋 Scheduled jobs:");
        Console.WriteLine("   - Test job: Every 2 minutes (SaveTodayResultAsync)");
        Console.WriteLine("   - Lottery job: Daily at 18:35 Vietnam Time (SaveTodayResultAsync)");
        Console.WriteLine($"📊 Dashboard: http://localhost:5000/admin/jobs");
    }
}