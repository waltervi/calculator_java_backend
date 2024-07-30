package com.company.backendcalculator.apis;

import com.company.backendcalculator.authorization_microservice.dto.RegisterRequest;
import com.company.backendcalculator.authorization_microservice.dto.RegisterResponse;
import com.company.backendcalculator.common.service.ObjectMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class AuthorizationAPI {

    private MockMvc mockMvc;

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    private ObjectMapperService objectMapperService;

    public RegisterResponse v1_auth_register_POST(String userName, String password, int status) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword(password);
        request.setUsername(userName);

        String req = objectMapperService.getObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/v1/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(req.getBytes())
        ).andReturn();
        assertEquals(result.getResponse().getStatus(),status);

        RegisterResponse registerResponse = null;
        if ( status == 200){
            registerResponse = objectMapperService.getObjectMapper().readValue(result.getResponse().getContentAsString(),RegisterResponse.class);
            return registerResponse;
        }

        return registerResponse;
    }

    public String v1_auth_login_POST(String userName, String password, int status) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword(password);
        request.setUsername(userName);

        String req = objectMapperService.getObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req.getBytes())).andReturn();
        assertEquals(result.getResponse().getStatus(),status);


        RegisterResponse registerResponse = null;
        if ( status == 200){
            registerResponse = objectMapperService.getObjectMapper().readValue(result.getResponse().getContentAsString(),RegisterResponse.class);
            return registerResponse.getToken();
        }

        return "";
    }

}
