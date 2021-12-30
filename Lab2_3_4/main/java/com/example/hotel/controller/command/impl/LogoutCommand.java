package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.entity.enums.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashSet;

import static com.example.hotel.controller.Constants.*;

public class LogoutCommand implements Command {
    private static final long serialVersionUID = -4242072042960675812L;

    @Override
    @SuppressWarnings("unchecked")
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            HashSet<Integer> loggedUsers = (HashSet<Integer>) session.getServletContext()
                    .getAttribute(ATTRIBUTE_LOGGED_USERS);


            if (loggedUsers != null) {
                Integer userId = (Integer) session.getAttribute(ATTRIBUTE_USER_ID);
                loggedUsers.remove(userId);
            }

            request.getSession(false).getServletContext()
                    .setAttribute(ATTRIBUTE_LOGGED_USERS, loggedUsers);

            session.invalidate();
        }
        request.getSession().setAttribute(ATTRIBUTE_USER_ROLE, UserRole.GUEST);

        return REDIRECT_TO + COMMAND_GET_MAIN;
    }
}
