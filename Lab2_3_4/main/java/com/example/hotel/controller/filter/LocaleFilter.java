package com.example.hotel.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

import static com.example.hotel.controller.Constants.*;

public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);

        if (session != null && session.getAttribute(ATTRIBUTE_LOCALE) == null) {
            Config.set(session, CONFIG_FMT_LOCALE, DEFAULT_LOCALE);
            session.setAttribute(ATTRIBUTE_LOCALE, DEFAULT_LOCALE);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
