package com.github.vdc2302.todo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Spring Security rejects requests inside its filters, before any controller runs,
 * so @RestControllerAdvice never sees those errors. This class forwards them to the
 * same resolver that GlobalExceptionHandler uses, so security errors get the same JSON body.
 *
 * - AuthenticationEntryPoint: not logged in / no valid token  -> 401
 * - AccessDeniedHandler:      logged in but wrong role        -> 403
 */
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

  private final HandlerExceptionResolver resolver;

  public SecurityErrorHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) {
    resolver.resolveException(request, response, null, ex);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) {
    resolver.resolveException(request, response, null, ex);
  }
}
