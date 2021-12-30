package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.hotel.controller.Constants.*;

public class GetApartmentPageCommand implements Command {
    private static final long serialVersionUID = -1022707793783981389L;
    private static final Logger logger = LogManager.getLogger(GetApartmentPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ApartmentService apartmentService = ServiceFactoryImpl.getInstance().getApartmentService();

        try {
            int apartmentId = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_APARTMENT_ID));
            Apartment apartment = apartmentService.getApartmentById(apartmentId);
            req.setAttribute(ATTRIBUTE_APARTMENT, apartment);
            req.setAttribute(REQ_PARAMETER_KEY_DATE_NOW, LocalDate.now());
            req.setAttribute(REQ_PARAMETER_KEY_DATE_TOMORROW, LocalDate.now().plusDays(1));

            int placeCount = Validator.returnPositiveIntIfValid(req.getParameter(REQ_PARAMETER_KEY_PLACE_COUNT));
            String dateIn = req.getParameter(REQ_PARAMETER_KEY_DATE_IN);
            String dateOut = req.getParameter(REQ_PARAMETER_KEY_DATE_OUT);
            LocalDate[] dates = Validator.dateInAndDateOutValidate(dateIn, dateOut, false);
            BigDecimal price = apartmentService.getApartmentPrice(apartmentId, dates[0], dates[1]);

            req.setAttribute(ATTRIBUTE_PRICE, price);
            req.setAttribute(REQ_PARAMETER_KEY_DATE_IN, dates[0]);
            req.setAttribute(REQ_PARAMETER_KEY_DATE_OUT, dates[1]);
            req.setAttribute(REQ_PARAMETER_KEY_PLACE_COUNT, placeCount);
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_INVALID_INPUT);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_ERROR);
            return PAGE_ERROR;
        }

        return PAGE_APARTMENT;
    }

}
