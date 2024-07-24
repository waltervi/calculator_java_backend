package com.company.backendcalculator.records.service;

import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.records.entities.Record;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordService {
    List<Record> findByUserId(Long userId, Pageable pageable);

    @Transactional
    void delete(UserData userData, Long id);

    @Transactional
    void create(UserData userData, Record record);
}
