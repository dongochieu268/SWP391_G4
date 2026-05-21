package com.edunac.mentora.config;

import com.edunac.mentora.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Object userObj = session.getAttribute("user");

        if (!(userObj instanceof User user) || user.getRoleId() == null || user.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
