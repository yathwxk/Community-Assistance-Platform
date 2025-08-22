package com.helpdesk.entity;

public enum RequestStatus {
    OPEN("Open"),
    ACCEPTED("Accepted"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    RequestStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
