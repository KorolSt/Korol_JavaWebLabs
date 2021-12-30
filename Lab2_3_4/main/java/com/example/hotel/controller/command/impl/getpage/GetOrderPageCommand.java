package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static com.example.hotel.controller.Constants.*;

public class GetOrderPageCommand implements Command {
    private static final long serialVersionUID = 2382082694592602207L;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute(REQ_PARAMETER_KEY_DATE_NOW, LocalDate.now());
        req.setAttribute(REQ_PARAMETER_KEY_DATE_TOMORROW, LocalDate.now().plusDays(1));
        return PAGE_ORDER;
    }
}
