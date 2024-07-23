package com.company.backendcalculator.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperService {
    ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
