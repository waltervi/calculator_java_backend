package com.company.backendcalculator;

import com.company.backendcalculator.operations.dto.OperationEnum;
import com.company.backendcalculator.operations.entities.Operation;
import com.company.backendcalculator.operations.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private OperationRepository operationRepository;

    public void run(ApplicationArguments args) {
        operationRepository.deleteAll();

        Operation op = new Operation();
        op.setCost(1d);
        op.setType(OperationEnum.addition.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(2d);
        op.setType(OperationEnum.substraction.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(3d);
        op.setType(OperationEnum.multiplication.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(4d);
        op.setType(OperationEnum.division.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(5d);
        op.setType(OperationEnum.square_root.toString());
        operationRepository.save(op);

        op = new Operation();
        op.setCost(6d);
        op.setType(OperationEnum.random_string.toString());
        operationRepository.save(op);

    }
}