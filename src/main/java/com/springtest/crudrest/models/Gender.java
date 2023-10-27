package com.springtest.crudrest.models;

public enum Gender {
    MALE ("Male", "M"),
    FEMALE("Female", "F");


    private final String title;
    private final String value;
    Gender(String title, String value) {
        this.title = title;
        this.value = value;
    }
    public String getTitle() {
        return title;
    }
    public String getValue() {
        return value;
    }

    public static Gender getValue(String value) {
        for(Gender v : values()) {
            if(v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
