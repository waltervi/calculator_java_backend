package com.company.backendcalculator.apis;

import com.company.backendcalculator.authorization.dto.RegisterRequest;
import com.company.backendcalculator.common.service.ObjectMapperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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

    public MockHttpServletResponse v1_auth_register_POST(String userName, String password, int status) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword(password);
        request.setUserName(userName);

        String req = objectMapperService.getObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/v1/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(req.getBytes())
        ).andReturn();
        assertEquals(result.getResponse().getStatus(),status);
        return result.getResponse();
    }

    public String v1_auth_login_POST(String userName, String password, int status) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword(password);
        request.setUserName(userName);

        String req = objectMapperService.getObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req.getBytes())).andReturn();
        assertEquals(result.getResponse().getStatus(),status);

        String token = result.getResponse().getCookie("token").getValue();
        assertTrue(token != null);

        return token;
    }

}
