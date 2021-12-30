package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.UserRole;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.EntityAlreadyExistsServiceException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class RegisterCommand implements Command {
    private static final long serialVersionUID = 4711564297767901913L;
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String page = COMMAND_GET_REGISTER + PARAMETER_KEY_MESSAGE;
        UserService userService = ServiceFactoryImpl.getInstance().getUserService();
        try {
            User user = validateAndBuildUser(req);
            userService.register(user);
            page += MESSAGE_SUCCESS;
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            page += MESSAGE_INVALID_INPUT;
        } catch (EntityAlreadyExistsServiceException e) {
            logger.error(e.getMessage());
            page += MESSAGE_USER_EXISTS;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            page += MESSAGE_ERROR;
        }
        return REDIRECT_TO + page;
    }

    private User validateAndBuildUser(HttpServletRequest req) throws InvalidInputException {
        String login = req.getParameter(REQ_PARAMETER_KEY_LOGIN);
        String password = req.getParameter(REQ_PARAMETER_KEY_PASSWORD);
        String name = req.getParameter(REQ_PARAMETER_KEY_NAME);
        String surname = req.getParameter(REQ_PARAMETER_KEY_SURNAME);
        String phone = req.getParameter(REQ_PARAMETER_KEY_PHONE);
        String email = req.getParameter(REQ_PARAMETER_KEY_EMAIL);

        Validator.loginAndPasswordValidate(login, password);

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(UserRole.CLIENT);
        user.setName(Validator.nameAndSurnameValidate(name, surname)[0]);
        user.setSurname(Validator.nameAndSurnameValidate(name, surname)[1]);
        user.setPhone(Validator.returnPhoneIfValid(phone));
        user.setEmail(Validator.returnEmailIfValid(email));

        return user;
    }
}
