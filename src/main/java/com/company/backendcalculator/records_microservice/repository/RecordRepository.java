package com.company.backendcalculator.records_microservice.repository;

import com.company.backendcalculator.records_microservice.entities.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecordRepository extends JpaRepository<Record,Long> {
    Page<Record> findByUserIdAndDeleted(Long title, Boolean deleted, Pageable pageable);
}
