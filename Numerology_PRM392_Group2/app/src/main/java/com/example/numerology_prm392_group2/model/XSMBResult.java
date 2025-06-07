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
public class XSMBResult {
    private String date;
    private String period;
    private XSMBPrizes prizes;
}
