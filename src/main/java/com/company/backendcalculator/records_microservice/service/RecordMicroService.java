package com.company.backendcalculator.records_microservice.service;

import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.records_microservice.entities.Record;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
* It is called like this to simulate a microservice call from operations microservice
* */
public interface RecordMicroService {
    List<Record> findByUserId(Long userId, Pageable pageable);

    @Transactional
    void delete(UserData userData, Long id);

    @Transactional
    void create(UserData userData, Record record);

    void rollbackCreate(UserData userData, Record record);
}
