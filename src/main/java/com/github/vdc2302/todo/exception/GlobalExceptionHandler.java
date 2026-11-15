package com.github.vdc2302.todo.exception;

import java.util.stream.Collectors;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": "  + e.getDefaultMessage()).collect(Collectors.joining(", "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
    }

    // ---- Security errors (401 = who are you?, 403 = not allowed) ----

    // Thrown by JwtAuthenticationFilter
    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredToken(ExpiredJwtException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Token has expired. Please log in again.");
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleInvalidToken(JwtException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid token.");
    }

    // Wrong username or password on /auth/login
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
    }

    // No token at all (from SecurityErrorHandler), or token for a user that no longer exists
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication required");
    }

    // Valid token but missing the required role, e.g. ROLE_USER calling /api/v1/admin/** (from SecurityErrorHandler)
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
    }
}
