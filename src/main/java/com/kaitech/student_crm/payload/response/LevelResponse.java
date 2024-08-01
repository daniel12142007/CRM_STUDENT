package com.kaitech.student_crm.payload.response;

public record LevelResponse(Long id,
                            String title,
                            String description,
                            Integer pointFrom,
                            Integer pointTo,
                            Integer point) {
    public LevelResponse(Long id, String title, String description, Integer pointFrom, Integer pointTo) {
        this(id, title, description, pointFrom, pointTo, null);
    }

    public LevelResponse(Long id, String title, Integer point) {
        this(id, title, null, null, null, point);
    }
}