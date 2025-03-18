package com.example.cleaning_service.exceptions;

import java.util.Map;

public class DuplicateFieldsException extends RuntimeException {
    private final Map<String, String> duplicateFields;

    public DuplicateFieldsException(Map<String, String> duplicateFields) {
        super("Duplicate fields found");
        this.duplicateFields = duplicateFields;
    }

    public Map<String, String> getDuplicateFields() {
        return duplicateFields;
    }
}