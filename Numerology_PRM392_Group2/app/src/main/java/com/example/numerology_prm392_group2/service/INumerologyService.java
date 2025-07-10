package com.example.numerology_prm392_group2.service;

import com.example.numerology_prm392_group2.models.XSMBResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface INumerologyService {
    @GET("khiemdoan/vietnam-lottery-xsmb-analysis/refs/heads/main/data/xsmb.json")
    Call<List<XSMBResult>> getAllXSMBResults();
}
