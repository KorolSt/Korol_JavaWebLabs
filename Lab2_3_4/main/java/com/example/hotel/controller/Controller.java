package com.example.hotel.controller;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.CommandFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class Controller extends HttpServlet {
    private static final long serialVersionUID = -1143327425265762392L;
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            process(request, response);
        } catch (IOException | ServletException e) {
            logger.error("Error during get request.", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            process(request, response);
        } catch (IOException | ServletException e) {
            logger.error("Error during post request.", e);
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String paramCommand = request.getParameter(REQ_PARAMETER_KEY_COMMAND);
        try {
            Command command = CommandFactory.get(paramCommand.toUpperCase());
            String page = command.execute(request, response);

            if (page.contains(REDIRECT_TO)) {
                response.sendRedirect(page.replace(REDIRECT_TO, ""));
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.error("Error processing request!", e);
            request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, "Server error! Please try again later!");
            request.getRequestDispatcher(PAGE_ERROR).forward(request, response);
        }
    }
}