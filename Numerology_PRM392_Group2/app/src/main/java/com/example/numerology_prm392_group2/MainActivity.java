//package com.example.numerology_prm392_group2;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        callApiAndConsoleData();
//    }
//    private void callApiAndConsoleData() {
//        Log.d("API", "Starting API call...");
//        NumerologyService.getAllXSMBResults()
//                .thenAccept(response -> {
//                    runOnUiThread(() -> {
//                        // Console ra response
//                        Log.d("API", "=== RESPONSE ===");
//                        Log.d("API", "Status: " + response.getStatus());
//                        Log.d("API", "Message: " + response.getMessage());
//
//                        // Console ra data
//                        if (response.getData() != null) {
//                            List<XSMBResult> data = response.getData();
//                            Log.d("API", "Data size: " + data.size());
//
//                            // Console từng item
//                            for (int i = 0; i < data.size(); i++) {
//                                Log.d("DATA", "Item " + i + ": " + data.get(i).toString());
//                            }
//                        } else {
//                            Log.d("API", "Data is null");
//                        }
//                        Log.d("API", "=== END ===");
//                    });
//                })
//                .exceptionally(error -> {
//                    runOnUiThread(() -> {
//                        Log.e("API", "Error: " + error.getMessage());
//                        error.printStackTrace();
//                    });
//                    return null;
//                });
//    }
//}