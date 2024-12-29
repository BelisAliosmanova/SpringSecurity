package com.springSecurity.springSecurity.repositories;

import com.springSecurity.springSecurity.enums.ExceptionSeverity;
import com.springSecurity.springSecurity.models.entity.Exception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExceptionRepository extends JpaRepository<Exception, UUID> {
    List<Exception> findAllBySeverityIs(ExceptionSeverity exceptionSeverity);
}
