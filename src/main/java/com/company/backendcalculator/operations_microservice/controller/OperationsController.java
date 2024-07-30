package com.company.backendcalculator.operations_microservice.controller;

import com.company.backendcalculator.operations_microservice.dto.OperationResponse;
import com.company.backendcalculator.operations_microservice.service.OperationService;
import com.company.backendcalculator.operations_microservice.dto.OperationEnum;
import com.company.backendcalculator.operations_microservice.dto.OperationRequest;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.service.TokenService;
import com.company.backendcalculator.common.dto.UserData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( originPatterns = "*,", allowCredentials = "true")
@RestController
public class OperationsController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/v1/operations")
    public OperationResponse executeOperation(
            @Valid @RequestBody OperationRequest body,
            @RequestHeader(value = "token") String token) {

        UserData userData = tokenService.decryptAndValidateToken(token);
        //begin validations
        OperationEnum operation = checkValidOperation(body.getOperation());

        Double operandOne = null;
        switch (operation) {
            case OperationEnum.ADDITION:
            case OperationEnum.SUBSTRACTION:
            case OperationEnum.MULTIPLICATION:
            case OperationEnum.DIVISION:
            case OperationEnum.SQUARE_ROOT:
                operandOne = checkValidNumericOperand(body.getOperand1());
        }

        Double operandTwo = null;
        switch (operation) {
            case OperationEnum.ADDITION:
            case OperationEnum.SUBSTRACTION:
            case OperationEnum.MULTIPLICATION:
            case OperationEnum.DIVISION:
                operandTwo = checkValidNumericOperand(body.getOperand2());
        }
        //end validations

        OperationResponse operationResponse = null;
        switch (operation) {
            case OperationEnum.ADDITION:
                operationResponse = operationService.addition(userData,operandOne,operandTwo);
                break;
            case OperationEnum.SUBSTRACTION:
                operationResponse = operationService.substraction(userData,operandOne,operandTwo);
                break;
            case OperationEnum.MULTIPLICATION:
                operationResponse = operationService.multiplication(userData,operandOne,operandTwo);
                break;
            case OperationEnum.DIVISION:
                operationResponse = operationService.division(userData,operandOne,operandTwo);
                break;
            case OperationEnum.SQUARE_ROOT:
                operationResponse = operationService.squareRoot(userData,operandOne);
                break;
            case OperationEnum.RANDOM_STRING:
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
