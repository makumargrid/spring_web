package com.example.springweb.servlet;

import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A plain Jakarta {@link HttpServlet} registered next to Spring MVC's
 * {@code DispatcherServlet} to illustrate manual servlet registration.
 * Mapped to {@code /servlet/info} via {@code ServletRegistrationBean}.
 */
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(
                "{\"servlet\":\"InfoServlet\",\"message\":\"Registered via ServletRegistrationBean\"}");
    }
}
