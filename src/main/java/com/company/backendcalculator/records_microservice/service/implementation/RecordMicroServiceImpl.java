package com.company.backendcalculator.records_microservice.service.implementation;

import com.company.backendcalculator.records_microservice.entities.Record;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.records_microservice.repository.RecordRepository;
import com.company.backendcalculator.records_microservice.service.RecordMicroService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * It is called like this to simulate a microservice call from operations microservice
 * */
@Service
public class RecordMicroServiceImpl implements RecordMicroService {
    @Autowired
    private RecordRepository recordRepository;

    @Override
    public List<Record> findByUserId(Long userId, Pageable pageable){
        //possible check if user has permissions
        Page<Record> page = recordRepository.findByUserIdAndDeleted(userId, false, pageable);
        return page.getContent();
    }

    @Override
    @Transactional
    public void delete(UserData userData, Long id){
        Record record = recordRepository.findById(id).orElseThrow(() -> new Http400Exception("record not found"));

        //check permissions
        if(record.getUserId() == null || userData.getUserId() != record.getUserId()){
            throw new Http401Exception();
        }
        record.setDeleted(true);
        recordRepository.save(record);
    }

    @Override
    @Transactional
    public void create(UserData userData, Record record){
        //In case of error, an error is expected to be thrown here
        // that in case would be returned as a 400 or 500 http status
        recordRepository.save(record);
    }

    @Override
    public void rollbackCreate(UserData userData, Record record) {
        //code that would simulate a transaction rollback called by a client
        // ( in this case Operations Microservice
    }

}
