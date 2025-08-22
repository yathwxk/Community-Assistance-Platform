package com.helpdesk.entity;

public enum RequestCategory {
    TOOLS("Tools & Equipment"),
    TUTORING("Tutoring & Education"),
    ERRANDS("Errands & Shopping"),
    TRANSPORTATION("Transportation"),
    HOUSEHOLD("Household Help"),
    GARDENING("Gardening"),
    TECHNOLOGY("Technology Help"),
    OTHER("Other");
    
    private final String displayName;
    
    RequestCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
