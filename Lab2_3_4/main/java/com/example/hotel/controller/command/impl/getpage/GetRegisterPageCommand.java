package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hotel.controller.Constants.PAGE_REGISTER;

public class GetRegisterPageCommand implements Command {
    private static final long serialVersionUID = -1520866306195923254L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        return PAGE_REGISTER;
    }
}
