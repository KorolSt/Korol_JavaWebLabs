package com.example.hotel.controller.command.impl;

import com.example.hotel.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class SetLocaleCommand implements Command {
    private static final long serialVersionUID = 8526959391942804791L;


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);

        String locale = req.getParameter(ATTRIBUTE_LOCALE);
        String sessionLocale = (String) session.getAttribute(ATTRIBUTE_LOCALE);

        String prevPage = req.getHeader("Referer").replaceAll(".*(?=/controller)", "");

        if (locale == null && sessionLocale == null) {
            Config.set(session, CONFIG_FMT_LOCALE, DEFAULT_LOCALE);
            session.setAttribute(ATTRIBUTE_LOCALE, DEFAULT_LOCALE);
        }

        Config.set(session, CONFIG_FMT_LOCALE, locale);
        session.setAttribute(ATTRIBUTE_LOCALE, locale);

        return REDIRECT_TO + prevPage;
    }
}
