package com.example.numerology_prm392_group2.manager;

import com.example.numerology_prm392_group2.models.GeneralAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralAgentManager {
    private static GeneralAgentManager instance;
    private Map<String, GeneralAgent> agents;

    private GeneralAgentManager() {
        agents = new HashMap<>();
        initializeDefaultAgents();
    }

    public static synchronized GeneralAgentManager getInstance() {
        if (instance == null) {
            instance = new GeneralAgentManager();
        }
        return instance;
    }

    private void initializeDefaultAgents() {
        // Add some default general agents
        addAgent(new GeneralAgent("Nguyễn Văn A", "0901234567", "nguyenvana@email.com", "123 Nguyễn Huệ, Q1, TP.HCM", 0.05));
        addAgent(new GeneralAgent("Trần Thị B", "0907654321", "tranthib@email.com", "456 Lê Lợi, Q1, TP.HCM", 0.04));
        addAgent(new GeneralAgent("Phạm Văn C", "0912345678", "phamvanc@email.com", "789 Hai Bà Trưng, Q3, TP.HCM", 0.06));
        addAgent(new GeneralAgent("Lê Thị D", "0918765432", "lethid@email.com", "321 Võ Văn Tần, Q3, TP.HCM", 0.05));
        addAgent(new GeneralAgent("Hoàng Văn E", "0923456789", "hoangvane@email.com", "654 Cách Mạng Tháng 8, Q10, TP.HCM", 0.07));
    }

    public void addAgent(GeneralAgent agent) {
        agents.put(agent.getAgentId(), agent);
    }

    public GeneralAgent getAgent(String agentId) {
        return agents.get(agentId);
    }

    public List<GeneralAgent> getAllAgents() {
        return new ArrayList<>(agents.values());
    }

    public List<GeneralAgent> getActiveAgents() {
        List<GeneralAgent> activeAgents = new ArrayList<>();
        for (GeneralAgent agent : agents.values()) {
            if (agent.isActive()) {
                activeAgents.add(agent);
            }
        }
        return activeAgents;
    }

    public void updateAgent(GeneralAgent agent) {
        agents.put(agent.getAgentId(), agent);
    }

    public void removeAgent(String agentId) {
        agents.remove(agentId);
    }

    public GeneralAgent findAgentByPhone(String phoneNumber) {
        for (GeneralAgent agent : agents.values()) {
            if (agent.getPhoneNumber().equals(phoneNumber)) {
                return agent;
            }
        }
        return null;
    }
}