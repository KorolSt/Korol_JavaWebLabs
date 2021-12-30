package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.ServiceFactory;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.example.hotel.controller.Constants.*;

public class GetMainPageCommand implements Command {
    private static final long serialVersionUID = 3304813743994497757L;
    private static final Logger logger = LogManager.getLogger(GetMainPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
        ApartmentService apartmentService = serviceFactory.getApartmentService();

        String paramApartmentType = req.getParameter(REQ_PARAMETER_KEY_APARTMENT_TYPE);

        try {
            ApartmentType apartmentType = Validator.returnTypeIfValid(paramApartmentType);
            List<Apartment> apartments;
            if (apartmentType != null) {
                apartments = apartmentService.getApartments(apartmentType);
            } else {
                apartments = apartmentService.getApartments();
            }

            req.setAttribute(ATTRIBUTE_APARTMENTS, apartments);
            req.setAttribute(ATTRIBUTE_DATE_IN, LocalDate.now());
            req.setAttribute(ATTRIBUTE_DATE_OUT, LocalDate.now().plusDays(1));
        } catch (InvalidInputException | ServiceException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, e.getMessage());
        }

        return PAGE_MAIN;
    }


}
