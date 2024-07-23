package com.company.backendcalculator.records.dto;

import com.company.backendcalculator.records.entities.Record;

import java.util.List;

public class RecordListResponse {
    private List<Record> records;

    public RecordListResponse() {}

    public RecordListResponse(List<Record> records) {
        this.records = records;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
