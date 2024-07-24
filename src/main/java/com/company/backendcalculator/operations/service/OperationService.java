package com.company.backendcalculator.operations.service;

import com.company.backendcalculator.authorization.entities.User;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.dto.OperationResponse;
import com.company.backendcalculator.operations.entities.Operation;
import jakarta.transaction.Transactional;

public interface OperationService {
    @Transactional
    OperationResponse addition(UserData userData, Double operandOne, Double operandTwo);

    @Transactional
    OperationResponse substraction(UserData userData, Double operandOne, Double operandTwo);

    @Transactional
    OperationResponse multiplication(UserData userData, Double operandOne, Double operandTwo);

    @Transactional
    OperationResponse division(UserData userData, Double operandOne, Double operandTwo);

    @Transactional
    OperationResponse squareRoot(UserData userData, Double operandOne);

    @Transactional
    OperationResponse randomString(UserData userData);

    @Transactional
    Double saveAndGetBalance(UserData userData, OperationCheckDto dto, String result);

    OperationCheckDto checkUserCanPerformOperation(UserData userData, OperationEnum operationEnum);

    String getRandomString();

    String asIntegerIfPossible(Double result);

    public static class OperationCheckDto {
        private User user;
        private Operation operation;

        public OperationCheckDto(User user, Operation operation) {
            this.user = user;
            this.operation = operation;
        }

        public User getUser() {
            return user;
        }

        public Operation getOperation() {
            return operation;
        }
    }
}
