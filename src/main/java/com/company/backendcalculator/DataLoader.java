package com.company.backendcalculator;

import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.entities.Operation;
import com.company.backendcalculator.operations.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    @Value("${cost_addition}")
    private Double costAddition;

    @Value("${cost_substraction}")
    private Double costSubstraction;

    @Value("${cost_multiplication}")
    private Double costMultiplication;

    @Value("${cost_division}")
    private Double costDivision;

    @Value("${cost_square_root}")
    private Double costSquareRoot;

    @Value("${cost_random_string}")
    private Double costRandomString;

    @Autowired
    private OperationRepository operationRepository;

    public void run(ApplicationArguments args) {
        operationRepository.deleteAll();

        Operation op = new Operation();
        op.setCost(costAddition);
        op.setType(OperationEnum.addition.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(costSubstraction);
        op.setType(OperationEnum.substraction.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(costMultiplication);
        op.setType(OperationEnum.multiplication.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(costDivision);
        op.setType(OperationEnum.division.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(costSquareRoot);
        op.setType(OperationEnum.square_root.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(costRandomString);
        op.setType(OperationEnum.random_string.toString());
        operationRepository.save(op);

    }
}