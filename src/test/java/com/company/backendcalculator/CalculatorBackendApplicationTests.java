package com.company.backendcalculator;

import com.company.backendcalculator.apis.AuthorizationAPI;
import com.company.backendcalculator.apis.OperationsAPI;
import com.company.backendcalculator.apis.RecordsAPI;
import com.company.backendcalculator.authorization.repository.UserRepository;
import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.dto.OperationResponse;
import com.company.backendcalculator.records.dto.RecordListResponse;
import com.company.backendcalculator.records.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
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

		operationsAPI.v1_operations_POST(OperationEnum.addition.name(),"1", "2", "", 401);

	}

	@Test
	void fullHappyPathTests() throws Exception {
		recordRepository.deleteAll();

		MockHttpServletResponse r1 = authorizationAPI.v1_auth_register_POST("john1","pass1",200);

		String token = r1.getCookie("token").getValue();
		assertTrue(token != null);

		token = authorizationAPI.v1_auth_login_POST("john1","pass1",200);

		OperationResponse resp;
		resp = operationsAPI.v1_operations_POST(OperationEnum.addition.name(),"1", "2", token, 200);
		assertEquals( Double.parseDouble(resp.getResult()) ,3.0);
		assertEquals(resp.getBalance(),999.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.substraction.name(),"2", "2", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),0);
		assertEquals(resp.getBalance(),997.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.multiplication.name(),"2", "4", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),8.0);
		assertEquals(resp.getBalance(),994.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.division.name(),"8", "8", token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),1.0);
		assertEquals(resp.getBalance(),990.0);


		resp = operationsAPI.v1_operations_POST(OperationEnum.square_root.name(),"4", null, token, 200);
		assertEquals(Double.parseDouble(resp.getResult()),2.0);
		assertEquals(resp.getBalance(),985.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.random_string.name(),null, null, token, 200);
		assertTrue(resp.getResult() != null);
		assertEquals(resp.getBalance(),979.0);

		resp = operationsAPI.v1_operations_POST(OperationEnum.random_string.name(),null, null, token, 200);
		assertTrue(resp.getResult() != null);
		assertEquals(resp.getBalance(),973.0);

		RecordListResponse r2 = recordsAPI.v1_records_GET(token,200);
		assertEquals(r2.getRecords().size(),7);
		long count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.addition.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.substraction.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.multiplication.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.division.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.square_root.name())).count();
		assertEquals(count, 1);

		count = r2.getRecords().stream().filter( p -> p.getOperationType().equals(OperationEnum.random_string.name())).count();
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

}
