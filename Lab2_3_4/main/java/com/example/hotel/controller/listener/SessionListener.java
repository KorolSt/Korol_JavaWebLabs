package com.example.hotel.controller.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

import static com.example.hotel.controller.Constants.ATTRIBUTE_LOGGED_USERS;
import static com.example.hotel.controller.Constants.ATTRIBUTE_USER_ID;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {}

    @Override
    @SuppressWarnings("unchecked")
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<Integer> loggedUsers = (HashSet<Integer>) httpSessionEvent
                .getSession().getServletContext()
                .getAttribute(ATTRIBUTE_LOGGED_USERS);
        Integer userId = (Integer) httpSessionEvent.getSession()
                .getAttribute(ATTRIBUTE_USER_ID);
        loggedUsers.remove(userId);
        httpSessionEvent.getSession().setAttribute(ATTRIBUTE_LOGGED_USERS, loggedUsers);
    }
}
