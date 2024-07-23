package com.company.backendcalculator.apis;

import com.company.backendcalculator.common.service.ObjectMapperService;
import com.company.backendcalculator.records.dto.RecordListResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class RecordsAPI {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapperService objectMapperService;

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public RecordListResponse v1_records_GET(String token, int status) throws Exception {
        Cookie tokenCookie = new Cookie("token", token);

        MvcResult result = mockMvc.perform(
                get("/v1/records")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(tokenCookie)
        ).andReturn();

        assertEquals(result.getResponse().getStatus(),status);

        if ( status == 200){
            RecordListResponse operationResponse = objectMapperService.getObjectMapper().readValue(result.getResponse().getContentAsString(),RecordListResponse.class);
            return operationResponse;
        }
        return  null;
    }

    public void v1_records_id_DELETE(Long id, String token, int status) throws Exception {
        Cookie tokenCookie = new Cookie("token", token);

        MvcResult result = mockMvc.perform(
                delete("/v1/records/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(tokenCookie)
        ).andReturn();

        assertEquals(result.getResponse().getStatus(),status);
    }

}
