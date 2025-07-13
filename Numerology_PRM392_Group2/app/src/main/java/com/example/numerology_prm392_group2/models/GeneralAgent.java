package com.example.numerology_prm392_group2.models;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeneralAgent {
    private String agentId;
    private String agentName;
    private String phoneNumber;
    private String email;
    private String address;
    private List<String> subAgentIds;
    private double commissionRate;
    private boolean isActive;
    private long createdAt;

    public GeneralAgent(String agentName, String phoneNumber, String email, String address, double commissionRate) {
        this.agentId = UUID.randomUUID().toString();
        this.agentName = agentName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.commissionRate = commissionRate;
        this.subAgentIds = new ArrayList<>();
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<String> getSubAgentIds() { return subAgentIds; }
    public void setSubAgentIds(List<String> subAgentIds) { this.subAgentIds = subAgentIds; }

    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public void addSubAgent(String subAgentId) {
        if (!subAgentIds.contains(subAgentId)) {
            subAgentIds.add(subAgentId);
        }
    }

    public void removeSubAgent(String subAgentId) {
        subAgentIds.remove(subAgentId);
    }
}