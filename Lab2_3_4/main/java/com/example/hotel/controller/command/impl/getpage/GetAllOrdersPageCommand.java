package com.example.hotel.controller.command.impl.getpage;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.validation.InvalidInputException;
import com.example.hotel.controller.command.validation.Validator;
import com.example.hotel.model.entity.Order;
import com.example.hotel.model.entity.enums.UserRole;
import com.example.hotel.model.service.OrderService;
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


public class GetAllOrdersPageCommand implements Command {
    private static final long serialVersionUID = -266722320936802343L;

    private static final Logger logger = LogManager.getLogger(GetAllOrdersPageCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        OrderService orderService = ServiceFactoryImpl.getInstance().getOrderService();

        String pSortBy = req.getParameter(REQ_PARAMETER_KEY_SORT);
        String pOrder = req.getParameter(REQ_PARAMETER_KEY_ORDER);

        try {
            Sorter sorter = Validator.returnSorterIfValid(pSortBy, pOrder);
            List<Order> orders;
            String pRecords = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_RECORDS_PER_PAGE), DEFAULT_RECORDS_PER_PAGE);
            String pCurrPage = Objects.requireNonNullElse(req.getParameter(REQ_PARAMETER_KEY_CURRENT_PAGE), DEFAULT_CURRENT_PAGE);
            int records = Integer.parseInt(pRecords);
            int currentPage = Integer.parseInt(pCurrPage);
            int rows;

            if (req.getSession(false).getAttribute(ATTRIBUTE_USER_ROLE).equals(UserRole.MANAGER)) {
                orders = orderService.getOrders(records, currentPage, sorter);
                rows = orderService.countRowsNumber();
            } else {
                int userId = (int) req.getSession(false).getAttribute(ATTRIBUTE_USER_ID);
                orders = orderService.getUserOrders(userId, currentPage, records, sorter);
                rows = orderService.countRowsNumberByUserId(userId);
            }

            int pagesCount = rows / records;

            pagesCount = (rows % records > 0 || pagesCount == 0) ? pagesCount + 1 : pagesCount;

            req.setAttribute(ATTRIBUTE_ORDERS, orders);
            req.setAttribute(ATTRIBUTE_CURRENT_PAGE, currentPage);
            req.setAttribute(ATTRIBUTE_RECORDS_PER_PAGE, records);
            req.setAttribute(ATTRIBUTE_PAGES_COUNT, pagesCount);
        } catch (InvalidInputException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_INVALID_INPUT);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            req.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MESSAGE_ERROR);
            return PAGE_ERROR;
        }

        return PAGE_ALL_ORDERS;
    }
}
