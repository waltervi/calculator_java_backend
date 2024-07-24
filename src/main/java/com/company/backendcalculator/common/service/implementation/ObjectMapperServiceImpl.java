package com.company.backendcalculator.common.service.implementation;

import com.company.backendcalculator.common.service.ObjectMapperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperServiceImpl implements ObjectMapperService {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @PostConstruct
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
