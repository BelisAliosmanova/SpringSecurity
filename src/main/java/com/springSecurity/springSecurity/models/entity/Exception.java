package com.springSecurity.springSecurity.models.entity;

import com.springSecurity.springSecurity.enums.ExceptionSeverity;
import com.springSecurity.springSecurity.exceptions.common.ApiException;
import com.springSecurity.springSecurity.models.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "exceptions")
public class Exception extends BaseEntity {
    private Integer statusCode;
    private String exceptionType;
    private String exceptionMessage;
    private String stackTraceString;
    private String methodName;
    private String className;
    private Integer lineNumber;
    @Enumerated(EnumType.STRING)
    private ExceptionSeverity severity;

    public static Exception mapFromRuntimeException(RuntimeException runtimeException, int statusCode) {
        // Exception Info
        String exceptionType = runtimeException.getClass().getName();
        String exceptionMessage = runtimeException.getMessage();

        // Stack Trace
        StackTraceElement[] stackTrace = runtimeException.getStackTrace();
        String stackTraceString = Arrays.toString(stackTrace);

        // Application Context
        String methodName = stackTrace[0].getMethodName();
        String className = stackTrace[0].getClassName();
        int lineNumber = stackTrace[0].getLineNumber();

        return Exception.builder()
                .statusCode(statusCode)
                .exceptionType(exceptionType)
                .exceptionMessage(exceptionMessage)
                .stackTraceString(stackTraceString)
                .methodName(methodName)
                .className(className)
                .lineNumber(lineNumber)
                .severity(ExceptionSeverity.CRITICAL)
                .build();
    }

    public static Exception mapFromApiException(ApiException apiException) {
        Exception exception = mapFromRuntimeException(apiException, apiException.getStatusCode());
        exception.setSeverity(ExceptionSeverity.INFORMATIONAL);

        return exception;
    }
}
