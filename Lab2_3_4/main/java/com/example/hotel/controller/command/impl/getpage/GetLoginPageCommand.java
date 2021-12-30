package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.PAGE_LOGIN;

public class GetLoginPageCommand implements Command {

    private static final long serialVersionUID = 3829177836686391753L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        return PAGE_LOGIN;
    }
}
