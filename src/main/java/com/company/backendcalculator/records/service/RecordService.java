package com.company.backendcalculator.records.service;

import com.company.backendcalculator.records.entities.Record;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.records.repository.RecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    public List<Record> findByUserId(Long userId, Pageable pageable){
        //possible check if user has permissions
        Page<Record> page = recordRepository.findByUserIdAndDeleted(userId, false, pageable);
        return page.getContent();
    }

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

    @Transactional
    public void create(UserData userData, Record record){
        recordRepository.save(record);
    }

}
