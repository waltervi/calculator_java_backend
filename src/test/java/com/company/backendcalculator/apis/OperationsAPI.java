package com.company.backendcalculator.apis;

import com.company.backendcalculator.common.service.ObjectMapperService;
import com.company.backendcalculator.operations_microservice.dto.OperationRequest;
import com.company.backendcalculator.operations_microservice.dto.OperationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class OperationsAPI {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapperService objectMapperService;

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public OperationResponse v1_operations_POST(String op, String op1, String op2, String token, int status) throws Exception {
        OperationRequest request = new OperationRequest();
        request.setOperation(op);
        request.setOperand1(op1);
        request.setOperand2(op2);

        String req = objectMapperService.getObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(
                post("/v1/operations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req.getBytes())
                .header("token", token)
        ).andReturn();

        assertEquals(result.getResponse().getStatus(),status);

        OperationResponse operationResponse = null;
        try{
            operationResponse = objectMapperService.getObjectMapper().readValue(result.getResponse().getContentAsString(),OperationResponse.class);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return  operationResponse;
    }


}
