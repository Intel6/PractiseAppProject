package com.example.android.taskbosstwo;

public class Note {

    private String title;
    private String description;
    private int priority;

    public Note() {
        //For Firebase
    }

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
