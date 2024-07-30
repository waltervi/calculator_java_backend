package com.company.backendcalculator.operations_microservice.repository;

import com.company.backendcalculator.operations_microservice.entities.Operation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//this one does not need pagination
@Repository
public interface OperationRepository extends CrudRepository<Operation,Long> {
    Operation findByType(String type);
}
