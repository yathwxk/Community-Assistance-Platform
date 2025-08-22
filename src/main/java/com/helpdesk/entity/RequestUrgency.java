package com.helpdesk.entity;

public enum RequestUrgency {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");
    
    private final String displayName;
    
    RequestUrgency(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
