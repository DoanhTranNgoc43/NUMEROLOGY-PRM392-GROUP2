package com.example.numerology_prm392_group2.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XSMBPrizes {
    private String specialPrize;      // Giải đặc biệt
    private String firstPrize;        // Giải nhất
    private List<String> secondPrize; // Giải nhì (2 giải)
    private List<String> thirdPrize;  // Giải ba (6 giải)
    private List<String> fourthPrize; // Giải tư (4 giải)
    private List<String> fifthPrize;  // Giải năm (6 giải)
    private List<String> sixthPrize;  // Giải sáu (3 giải)
    private List<String> seventhPrize; // Giải bảy (4 giải)
}
