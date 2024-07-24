package com.company.backendcalculator.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

public interface ObjectMapperService {
    @PostConstruct
    void init();

    ObjectMapper getObjectMapper();
}
