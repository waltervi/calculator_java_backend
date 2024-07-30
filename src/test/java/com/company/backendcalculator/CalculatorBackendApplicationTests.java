package com.company.backendcalculator;

import com.company.backendcalculator.apis.AuthorizationAPI;
import com.company.backendcalculator.apis.OperationsAPI;
import com.company.backendcalculator.apis.RecordsAPI;
import com.company.backendcalculator.authorization_microservice.dto.RegisterResponse;
import com.company.backendcalculator.authorization_microservice.repository.UserRepository;
import com.company.backendcalculator.operations_microservice.dto.OperationEnum;
import com.company.backendcalculator.operations_microservice.dto.OperationResponse;
import com.company.backendcalculator.records_microservice.dto.RecordListResponse;
import com.company.backendcalculator.records_microservice.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CalculatorBackendApplicationTests {

	@Autowired
	AuthorizationAPI authorizationAPI;

	@Autowired
	OperationsAPI operationsAPI;

	@Autowired
	RecordsAPI recordsAPI;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void beforeEach(){
		authorizationAPI.setMockMvc(mockMvc);
		operationsAPI.setMockMvc(mockMvc);
		recordsAPI.setMockMvc(mockMvc);
	}

	@Test
	void duplicatedUserRegistration() throws Exception {
		userRepository.deleteAll();

		authorizationAPI.v1_auth_register_POST("john","pass1", 200);
		authorizationAPI.v1_auth_register_POST("john","pass2", 500);
	}

	@Test
	void operationsWithoutToken() throws Exception {
		operationsAPI.v1_operations_POST(OperationEnum.ADDITION.name(),"1", "2", "", 401);
	}

	@Test
	void fullHappyPathTests() throws Exception {
		recordRepository.deleteAll();

		RegisterResponse r1 = authorizationAPI.v1_auth_register_POST("john1","pass1",200);

		String token = r1.getToken();
		assertTrue(token != null);

		token = authorizationAPI.v1_auth_login_POST("john1","pass1",200);

		OperationResponse resp;
		resp = operationsAPI.v1_operations_POST(OperationEnum.ADDITION.name(),"1", "2", token, 200);
		assertEquals( Double.parseDouble(resp.getResult()) ,3.0);
		assertEquals(resp.getBalance(),49.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.SUBSTRACTION.name(),"2", "2", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),0);
		assertEquals(resp.getBalance(),47.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.MULTIPLICATION.name(),"2", "4", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),8.0);
		assertEquals(resp.getBalance(),44.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.DIVISION.name(),"8", "8", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),1.0);
		assertEquals(resp.getBalance(),40.0);


		resp = operationsAPI.v1_operations_POST(OperationEnum.SQUARE_ROOT.name(),"4", null, token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),2.0);
		assertEquals(resp.getBalance(),35.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.RANDOM_STRING.name(),null, null, token, 200);
		assertTrue(resp.getResult() != null);
		assertEquals(resp.getBalance(),29.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.RANDOM_STRING.name(),null, null, token, 200);
		assertTrue(resp.getResult() != null);
		assertEquals(resp.getBalance(),23.0);

		RecordListResponse r2 = recordsAPI.v1_records_GET(token,200);
		assertEquals(r2.getRecords().size(),7);
		long count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.ADDITION.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.SUBSTRACTION.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.MULTIPLICATION.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.DIVISION.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.SQUARE_ROOT.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.RANDOM_STRING.name())).count();
		assertEquals(count, 2);


		Long id0 = r2.getRecords().get(0).getId();
		Long id1 = r2.getRecords().get(3).getId();
		Long id2 = r2.getRecords().get(4).getId();
		recordsAPI.v1_records_id_DELETE(id0,token,200);
		recordsAPI.v1_records_id_DELETE(id1,token,200);
		recordsAPI.v1_records_id_DELETE(id2,token,200);

		r2 = recordsAPI.v1_records_GET(token,200);
		assertEquals(r2.getRecords().size(),4);

		count = r2.getRecords().stream().filter( p -> p.getId() == id0).count();
		assertEquals(count,0);

		count = r2.getRecords().stream().filter( p -> p.getId() == id1).count();
		assertEquals(count,0);

		count = r2.getRecords().stream().filter( p -> p.getId() == id2).count();
		assertEquals(count,0);

	}


	@Test
	void edgeCases() throws Exception {
		userRepository.deleteAll();
		RegisterResponse r1 = authorizationAPI.v1_auth_register_POST("edgeCaseUser1","edgeCaseUser1",200);

		String token = r1.getToken();
		assertTrue(token != null);


		//Edge case 1: Division by zero
		OperationResponse resp = operationsAPI.v1_operations_POST(OperationEnum.DIVISION.name(),"1", "0", token, 400);
		assertEquals(resp.getErrorMessage(), "Error: Division by zero");

		//Edge case 1: Division by zero
		resp = operationsAPI.v1_operations_POST(OperationEnum.SQUARE_ROOT.name(),"-1","", token, 400);
		assertEquals(resp.getErrorMessage(), "Error: Negative input for square root");

		resp = operationsAPI.v1_operations_POST(OperationEnum.SQUARE_ROOT.name(),"zzzz","", token, 400);
		assertEquals(resp.getErrorMessage(), "invalid operand value");

	}

}
