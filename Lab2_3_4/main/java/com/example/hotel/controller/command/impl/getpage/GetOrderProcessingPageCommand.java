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
import java.util.List;

import static com.example.hotel.controller.Constants.*;

public class GetOrderProcessingPageCommand implements Command {
    private static final long serialVersionUID = 2891356446489506670L;

    private static final Logger logger = LogManager.getLogger(GetOrderProcessingPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String paramOrderId = req.getParameter(REQ_PARAMETER_KEY_ORDER_ID);
        ApartmentService apartmentService = ServiceFactoryImpl.getInstance().getApartmentService();

        try {
            int orderId = Validator.returnPositiveIntIfValid(paramOrderId);

            List<Apartment> apartments = apartmentService.getFreeApartments(orderId);
            req.setAttribute(ATTRIBUTE_APARTMENTS, apartments);
            req.setAttribute(ATTRIBUTE_ORDER_ID, orderId);
        } catch (InvalidInputException | ServiceException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, e.getMessage());
        }

        return PAGE_ORDER_PROCESS;
    }
}
