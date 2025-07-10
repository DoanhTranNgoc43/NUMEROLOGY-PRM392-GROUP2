package com.example.numerology_prm392_group2.service;

import com.example.numerology_prm392_group2.models.Response;
import com.example.numerology_prm392_group2.models.XSMBResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NumerologyService {
    private static final INumerologyService api =
            ApiClient.getClient().create(INumerologyService.class);

    public static CompletableFuture<Response<List<XSMBResult>>> getAllXSMBResults() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var call = api.getAllXSMBResults();
                var response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    return Response.<List<XSMBResult>>builder()
                            .message("XSMB results fetched successfully")
                            .status("SUCCESS")
                            .data(response.body())
                            .build();
                }

                return Response.<List<XSMBResult>>builder()
                        .message("Failed to fetch XSMB results")
                        .status("ERROR")
                        .build();
            } catch (Exception e) {
                return Response.<List<XSMBResult>>builder()
                        .message("Error: " + e.getMessage())
                        .status("ERROR")
                        .build();
            }
        });
    }
}
