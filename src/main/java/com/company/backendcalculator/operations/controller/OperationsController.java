package com.company.backendcalculator.operations.controller;

import com.company.backendcalculator.operations.dto.OperationResponse;
import com.company.backendcalculator.operations.service.OperationService;
import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.dto.OperationRequest;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.authorization.service.TokenService;
import com.company.backendcalculator.common.dto.UserData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationsController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/v1/operations")
    public OperationResponse executeOperation(
            @Valid @RequestBody OperationRequest body,
            @CookieValue(name = "token") String token) {

        UserData userData = tokenService.decryptAndValidateToken(token);
        //begin validations
        OperationEnum operation = checkValidOperation(body.getOperation());

        Double operandOne = null;
        switch (operation) {
            case OperationEnum.addition:
            case OperationEnum.substraction:
            case OperationEnum.multiplication:
            case OperationEnum.division:
            case OperationEnum.square_root:
                operandOne = checkValidNumericOperand(body.getOperand1());
        }

        Double operandTwo = null;
        switch (operation) {
            case OperationEnum.addition:
            case OperationEnum.substraction:
            case OperationEnum.multiplication:
            case OperationEnum.division:
                operandTwo = checkValidNumericOperand(body.getOperand2());
        }
        //end validations

        OperationResponse operationResponse = null;
        switch (operation) {
            case OperationEnum.addition:
                operationResponse = operationService.addition(userData,operandOne,operandTwo);
                break;
            case OperationEnum.substraction:
                operationResponse = operationService.substraction(userData,operandOne,operandTwo);
                break;
            case OperationEnum.multiplication:
                operationResponse = operationService.multiplication(userData,operandOne,operandTwo);
                break;
            case OperationEnum.division:
                operationResponse = operationService.division(userData,operandOne,operandTwo);
                break;
            case OperationEnum.square_root:
                operationResponse = operationService.squareRoot(userData,operandOne);
                break;
            case OperationEnum.random_string:
                operationResponse = operationService.randomString(userData);
                break;
            default:
                throw new Http400Exception("invalid operation value");
        }
        return operationResponse;
    }


    private OperationEnum checkValidOperation(String operation) {
        if (operation == null) {
            throw new Http400Exception("invalid operation value");
        }
        OperationEnum operationEnum;
        try{
            operationEnum = OperationEnum.valueOf(operation);
        }
        catch (Exception ex){
            throw new Http400Exception("invalid operation value");
        }
        return operationEnum;
    }

    private Double checkValidNumericOperand(String s) {
        Double d = null;
        try {
            d = Double.parseDouble(s);
        } catch (Exception e) {
            throw new Http400Exception("invalid operand value");
        }
        return d;
    }

}
