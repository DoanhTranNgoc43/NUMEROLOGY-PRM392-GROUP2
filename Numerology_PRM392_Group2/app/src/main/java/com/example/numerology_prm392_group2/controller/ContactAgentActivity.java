package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.numerology_prm392_group2.R;

public class ContactAgentActivity extends AppCompatActivity {

    private static final String TAG = "ContactAgentActivity";
    private static final String AGENT_PHONE_NUMBER = "tel:+0348956373";
    private static final String AGENT_SMS_NUMBER = "sms:+0348956373";
    private static final String AGENT_EMAIL = "doanhtnhe172637@fpt.edu.vn";
    private AppCompatButton btnCallAgent;
    private AppCompatButton btnSendMessegerAgent;
    private AppCompatButton btnSendEmailAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_agent);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setupClickListeners();
    }

    private void initializeComponents() {
        btnCallAgent = findViewById(R.id.btnCallAgent);
        btnSendMessegerAgent = findViewById(R.id.btnSendMessegerAgent);
        btnSendEmailAgent = findViewById(R.id.btnSendEmailAgent);
    }

    private void setupClickListeners() {
        btnCallAgent.setOnClickListener(v -> {
            Log.d(TAG, "Call Agent button clicked");
            initiatePhoneCall();
        });

        btnSendMessegerAgent.setOnClickListener(v -> {
            Log.d(TAG, "Send Message Agent button clicked");
            initiateMessage();
        });

        btnSendEmailAgent.setOnClickListener(v -> {
            Log.d(TAG, "Send Email Agent button clicked");
            initiateEmail();
        });
    }

    private void initiatePhoneCall() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(AGENT_PHONE_NUMBER));
            startActivity(callIntent);
            Log.d(TAG, "Initiating phone call to " + AGENT_PHONE_NUMBER);
        } catch (Exception e) {
            Log.e(TAG, "Error initiating phone call", e);
            Toast.makeText(this, "Không thể thực hiện cuộc gọi", Toast.LENGTH_SHORT).show();
        }
    }

    private void initiateMessage() {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse(AGENT_SMS_NUMBER));
            startActivity(smsIntent);
            Log.d(TAG, "Initiating SMS to " + AGENT_SMS_NUMBER);
        } catch (Exception e) {
            Log.e(TAG, "Error initiating SMS", e);
            Toast.makeText(this, "Không thể gửi tin nhắn", Toast.LENGTH_SHORT).show();
        }
    }

    private void initiateEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + AGENT_EMAIL));
            startActivity(emailIntent);
            Log.d(TAG, "Initiating email to " + AGENT_EMAIL);
        } catch (Exception e) {
            Log.e(TAG, "Error initiating email", e);
            Toast.makeText(this, "Không thể gửi email", Toast.LENGTH_SHORT).show();
        }
    }
}