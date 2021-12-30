package com.example.hotel.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public interface Command extends Serializable {
    String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}