package com.company.backendcalculator.records_microservice.controller;

import com.company.backendcalculator.common.service.TokenService;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.records_microservice.dto.RecordListResponse;
import com.company.backendcalculator.records_microservice.entities.Record;
import com.company.backendcalculator.records_microservice.service.RecordMicroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin( originPatterns = "*,", allowCredentials = "true")
@RestController
public class RecordController {

    @Autowired
    private RecordMicroService recordMicroService;

    @Autowired
    private TokenService tokenService;

    @GetMapping(value = "/v1/records")
    public ResponseEntity<RecordListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestHeader(value = "token") String token) {

        UserData userData = tokenService.decryptAndValidateToken(token);

        Pageable paging = PageRequest.of(page, size);

        List<Record> recordList = recordMicroService.findByUserId(userData.getUserId(),paging);
        RecordListResponse response = new RecordListResponse(recordList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/v1/records/{id}")
    public void delete(@PathVariable(value = "id") Long id,
                       @RequestHeader(value = "token") String token) {

        UserData userData = tokenService.decryptAndValidateToken(token);

        recordMicroService.delete(userData,id);
    }
}
