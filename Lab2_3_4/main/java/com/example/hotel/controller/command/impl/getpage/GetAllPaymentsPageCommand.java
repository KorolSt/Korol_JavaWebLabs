package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Payment;
import com.example.hotel.model.entity.enums.UserRole;
import com.example.hotel.model.service.PaymentService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.hotel.controller.Constants.*;

public class GetAllPaymentsPageCommand implements Command {
    private static final long serialVersionUID = 3498474417258487956L;

    private static final Logger logger = LogManager.getLogger(GetAllPaymentsPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PaymentService paymentService = ServiceFactoryImpl.getInstance().getPaymentService();
        String pSortBy = req.getParameter(REQ_PARAMETER_KEY_SORT);
        String pOrder = req.getParameter(REQ_PARAMETER_KEY_ORDER);

        try {
            Sorter sorter = Validator.returnSorterIfValid(pSortBy, pOrder);
            String pRecords = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_RECORDS_PER_PAGE), DEFAULT_RECORDS_PER_PAGE);
            String pCurrPage = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_CURRENT_PAGE), DEFAULT_CURRENT_PAGE);
            List<Payment> payments;

            int records = Integer.parseInt(pRecords);
            int currentPage = Integer.parseInt(pCurrPage);
            int rows;

            if (req.getSession(false).getAttribute(ATTRIBUTE_USER_ROLE).equals(UserRole.MANAGER)) {
                payments = paymentService.getPayments(records, currentPage, sorter);
                rows = paymentService.countRowsNumber();
            } else {
                int userId = (int) req.getSession(false).getAttribute(ATTRIBUTE_USER_ID);
                payments = paymentService.getUserPayments(userId, records, currentPage, sorter);
                rows = paymentService.countRowsNumberByUserId(userId);
            }

            int pagesCount = rows / records;

            req.setAttribute(ATTRIBUTE_PAYMENTS, payments);

            if (rows % records > 0 || pagesCount == 0) {
                pagesCount++;
            }

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

        return PAGE_ALL_PAYMENTS;
    }
}
