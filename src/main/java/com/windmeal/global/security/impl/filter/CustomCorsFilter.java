package com.windmeal.global.security.impl.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

  @Value("${domain.url.local}")
  private String local;
  @Value("${domain.url.ws_host}")
  private String ws_host;
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    response.setHeader("Access-Control-Allow-Origin", local + " , " + ws_host);
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods",
        "HEAD, GET, POST, PUT, DELETE, PATCH, OPTIONS");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Authorization, X-Requested-With, requestId, Correlation-Id");
    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
