package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Reservation;
import com.example.hotel.model.entity.enums.UserRole;
import com.example.hotel.model.service.ReservationService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.hotel.controller.Constants.*;

public class GetAllReservationsPageCommand implements Command {
    private static final long serialVersionUID = 3621977950191010089L;

    private static final Logger logger = LogManager.getLogger(GetAllReservationsPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ReservationService reservationService = ServiceFactoryImpl.getInstance().getReservationService();
        String pSortBy = req.getParameter(REQ_PARAMETER_KEY_SORT);
        String pOrder = req.getParameter(REQ_PARAMETER_KEY_ORDER);

        try {
            Sorter sorter = Validator.returnSorterIfValid(pSortBy, pOrder);
            String pRecords = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_RECORDS_PER_PAGE), DEFAULT_RECORDS_PER_PAGE);
            String pCurrPage = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_CURRENT_PAGE), DEFAULT_CURRENT_PAGE);
            int records = Integer.parseInt(pRecords);
            int currentPage = Integer.parseInt(pCurrPage);
            int rows;
            List<Reservation> reservations;

            HttpSession session = req.getSession(false);

            if (session.getAttribute(ATTRIBUTE_USER_ROLE).equals(UserRole.MANAGER)) {
                reservations = reservationService.getReservations(records, currentPage, sorter);
                rows = reservationService.countRowsNumber();
            } else {
                int userId = (int) session.getAttribute(ATTRIBUTE_USER_ID);
                reservations = reservationService.getUserReservations(userId, currentPage, records, sorter);
                rows = reservationService.countRowsNumberByUserId(userId);
            }

            int pagesCount = rows / records;
            if (rows % records > 0 || pagesCount == 0)
                pagesCount++;

            req.setAttribute(ATTRIBUTE_RESERVATIONS, reservations);
            req.setAttribute(ATTRIBUTE_PAGES_COUNT, pagesCount);
            req.setAttribute(ATTRIBUTE_CURRENT_PAGE, currentPage);
            req.setAttribute(ATTRIBUTE_RECORDS_PER_PAGE, records);
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_INVALID_INPUT);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_ERROR);
            return PAGE_ERROR;
        }

        return PAGE_ALL_RESERVATION;
    }
}
