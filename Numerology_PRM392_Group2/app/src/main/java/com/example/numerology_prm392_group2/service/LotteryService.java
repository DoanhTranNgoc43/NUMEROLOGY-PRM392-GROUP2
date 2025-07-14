package com.example.numerology_prm392_group2.service;

import android.util.Log;
import com.example.numerology_prm392_group2.models.LotteryResult;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LotteryService {
    private static final String TAG = "LotteryService";
    private static final String CSV_URL = "https://raw.githubusercontent.com/khiemdoan/vietnam-lottery-xsmb-analysis/main/data/xsmb-2-digits.csv";
    private static LotteryService instance;
    private OkHttpClient client;

    private LotteryService() {
        client = new OkHttpClient();
    }

    public static synchronized LotteryService getInstance() {
        if (instance == null) {
            instance = new LotteryService();
        }
        return instance;
    }

    public interface LotteryResultCallback {
        void onSuccess(LotteryResult result);
        void onError(String error);
    }

    public void getTodayResult(LotteryResultCallback callback) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        getResultByDate(today, callback);
    }

    public void getResultByDate(String date, LotteryResultCallback callback) {
        Request request = new Request.Builder()
                .url(CSV_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch CSV", e);
                callback.onError("Không thể tải kết quả xổ số: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Lỗi server: " + response.code());
                    return;
                }

                String csvContent = response.body().string();
                Log.d(TAG, "CSV received:\n" + csvContent);

                try {
                    LotteryResult result = parseResultFromCsv(csvContent, date);
                    if (result == null) {
                        callback.onError("Không tìm thấy kết quả cho ngày " + date);
                    } else {
                        callback.onSuccess(result);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to parse CSV", e);
                    callback.onError("Lỗi phân tích dữ liệu: " + e.getMessage());
                }
            }
        });
    }

    private LotteryResult parseResultFromCsv(String csvContent, String targetDate) {
        LotteryResult result = new LotteryResult();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String[] lines = csvContent.split("\n");
        if (lines.length < 2) return null;

        for (int i = 1; i < lines.length; i++) {
            String[] columns = lines[i].trim().split(",", -1);
            if (columns.length < 28) continue; // 1 cột ngày + 27 số

            if (!columns[0].equals(targetDate)) continue;

            try {
                result.setDrawDate(sdf.parse(columns[0]));
                result.setResultId(targetDate);

                int index = 1;

                result.setSpecialPrize(columns[index++]);
                result.setFirstPrize(columns[index++]);

                result.setSecondPrizes(Arrays.asList(columns[index++], columns[index++]));
                result.setThirdPrizes(Arrays.asList(columns[index++], columns[index++], columns[index++],
                        columns[index++], columns[index++], columns[index++]));
                result.setFourthPrizes(Arrays.asList(columns[index++], columns[index++],
                        columns[index++], columns[index++]));
                result.setFifthPrizes(Arrays.asList(columns[index++], columns[index++], columns[index++],
                        columns[index++], columns[index++], columns[index++]));
                result.setSixthPrizes(Arrays.asList(columns[index++], columns[index++], columns[index++]));
                result.setSeventhPrizes(Arrays.asList(columns[index++], columns[index++],
                        columns[index++], columns[index++]));

                return result;

            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi parse dòng CSV đơn giản", e);
            }
        }

        return null;
    }


    public LotteryResult getMockResult() {
        LotteryResult result = new LotteryResult();
        result.setResultId("mock");
        result.setDrawDate(new Date());
        result.setSpecialPrize("45");
        return result;
    }
}
