package com.oceanview.resort.presentation.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanview.resort.controller.AuthController;
import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.dto.LoginRequestDTO;
import com.oceanview.resort.dto.LoginResponseDTO;
import com.oceanview.resort.factory.DAOFactory;
import com.oceanview.resort.service.AuthService;
import com.oceanview.resort.service.impl.AuthServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String VERSION = "LoginServlet v5.0";
    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

    private final ObjectMapper mapper = new ObjectMapper();
    private AuthController authController;

    @Override
    public void init() throws ServletException {
        SystemUserDAO systemUserDAO = DAOFactory.getSystemUserDAO();
        AuthService authService = new AuthServiceImpl(systemUserDAO);
        this.authController = new AuthController(authService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        writeJson(resp, HttpServletResponse.SC_OK,
                fail(VERSION + " GET OK. Use POST /login with JSON: {\"username\":\"...\",\"password\":\"...\"}"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // Read bytes ONCE, parse from the same bytes.
        byte[] raw = req.getInputStream().readAllBytes();

        if (raw.length == 0) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Missing JSON body"));
            return;
        }

        final LoginRequestDTO loginReq;
        try {
            loginReq = mapper.readValue(raw, LoginRequestDTO.class);
        } catch (JsonProcessingException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Invalid JSON"));
            return;
        } catch (IOException ex) {
            // extra safety: treat any parse/read issue as bad request
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Invalid JSON"));
            return;
        }

        if (isBlank(loginReq.getUsername()) || isBlank(loginReq.getPassword())) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("username and password are required"));
            return;
        }

        final LoginResponseDTO dto;
        try {
            dto = authController.login(loginReq);
        } catch (RuntimeException ex) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
            return;
        }

        // Create session ONLY on success (logout invalidates this).
        if (dto != null && dto.isSuccess()) {
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", dto.getUserId());
            session.setAttribute("username", dto.getUsername());
            session.setAttribute("role", dto.getRole());
            session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);

            writeJson(resp, HttpServletResponse.SC_OK, dto);
            return;
        }

        writeJson(resp, HttpServletResponse.SC_UNAUTHORIZED,
                dto != null ? dto : fail("Invalid credentials"));
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void writeJson(HttpServletResponse resp, int status, LoginResponseDTO dto) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        mapper.writeValue(resp.getOutputStream(), dto);
    }

    private LoginResponseDTO fail(String message) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setSuccess(false);
        dto.setUserId(null);
        dto.setUsername(null);
        dto.setRole(null);
        dto.setMessage(message);
        return dto;
    }
}