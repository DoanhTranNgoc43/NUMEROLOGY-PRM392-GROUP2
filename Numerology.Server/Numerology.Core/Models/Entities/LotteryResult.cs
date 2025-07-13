namespace Numerology.Core.Models.Entities;

public class LotteryResult : BaseEntity
{
        public DateTime Date { get; set; }
        // Giải đặc biệt
        public int Special { get; set; }

        // Giải nhất
        public int Prize1 { get; set; }

        // Giải nhì
        public int Prize2_1 { get; set; }
        public int Prize2_2 { get; set; }

        // Giải ba
        public int Prize3_1 { get; set; }
        public int Prize3_2 { get; set; }
        public int Prize3_3 { get; set; }
        public int Prize3_4 { get; set; }
        public int Prize3_5 { get; set; }
        public int Prize3_6 { get; set; }

        // Giải tư
        public int Prize4_1 { get; set; }
        public int Prize4_2 { get; set; }
        public int Prize4_3 { get; set; }
        public int Prize4_4 { get; set; }

        // Giải năm
        public int Prize5_1 { get; set; }
        public int Prize5_2 { get; set; }
        public int Prize5_3 { get; set; }
        public int Prize5_4 { get; set; }
        public int Prize5_5 { get; set; }
        public int Prize5_6 { get; set; }

        // Giải sáu
        public int Prize6_1 { get; set; }
        public int Prize6_2 { get; set; }
        public int Prize6_3 { get; set; }

        // Giải bảy
        public int Prize7_1 { get; set; }
        public int Prize7_2 { get; set; }
        public int Prize7_3 { get; set; }
        public int Prize7_4 { get; set; }
}