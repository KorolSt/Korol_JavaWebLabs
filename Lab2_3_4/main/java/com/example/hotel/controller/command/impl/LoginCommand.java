package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.ServiceFactory;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.IncorrectCredentialsServiceException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;

import static com.example.hotel.controller.Constants.*;

public class LoginCommand implements Command {

    private static final long serialVersionUID = -6622721626752105188L;
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_LOGIN;

        String login = req.getParameter(REQ_PARAMETER_KEY_LOGIN);
        String password = req.getParameter(REQ_PARAMETER_KEY_PASSWORD);

        ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            User user = userService.login(login, password);
            if (!checkUserIsLogged(req, user.getId())) {
                setUserRoleAndId(req, user);
                page = COMMAND_GET_MAIN;
            } else {
                page += PARAMETER_KEY_MESSAGE + MESSAGE_USER_LOGGED;
            }
        } catch (IncorrectCredentialsServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_INCORRECT_CREDENTIALS;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += PARAMETER_KEY_MESSAGE + MESSAGE_ERROR;
        }

        return REDIRECT_TO + page;
    }

    private void setUserRoleAndId(HttpServletRequest request, User user) {
        ServletContext context = request.getServletContext();
        context.setAttribute(ATTRIBUTE_USER_ID, user.getId());

        HttpSession session = request.getSession(false);
        session.setAttribute(ATTRIBUTE_USER_ROLE, user.getRole());
        session.setAttribute(ATTRIBUTE_USER_ID, user.getId());
    }

    @SuppressWarnings("unchecked")
    private boolean checkUserIsLogged(HttpServletRequest request,  Integer userId){
        HashSet<Integer> loggedUsers = (HashSet<Integer>) request.getSession(false).getServletContext()
                .getAttribute(ATTRIBUTE_LOGGED_USERS);

        if (loggedUsers == null) {
            loggedUsers = new HashSet<>();
            request.getSession(false).setAttribute(ATTRIBUTE_LOGGED_USERS, loggedUsers);
        }

        if(loggedUsers.stream().anyMatch(userId::equals)){
            return true;
        }

        loggedUsers.add(userId);
        request.getSession(false).getServletContext()
                .setAttribute(ATTRIBUTE_LOGGED_USERS, loggedUsers);
        return false;
    }
}
