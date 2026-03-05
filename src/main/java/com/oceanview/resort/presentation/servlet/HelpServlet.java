package com.oceanview.resort.presentation.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("app", "OceanView Resort RMS");
        out.put("status", "OK");
        out.put("endpoints", new String[] {
                "POST /auth/login",
                "POST /auth/logout",
                "POST /reservations",
                "GET  /reservations?reservationNo=...",
                "PUT  /reservations",
                "DELETE /reservations?reservationNo=... (Admin only)",
                "POST /billing (generate invoice)",
                "GET  /billing?reservationNo=..."
        });

        mapper.writeValue(resp.getOutputStream(), out);
    }
}