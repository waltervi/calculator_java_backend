package com.company.backendcalculator.authorization_microservice.repository;

import com.company.backendcalculator.authorization_microservice.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUserName(String userName);
}
