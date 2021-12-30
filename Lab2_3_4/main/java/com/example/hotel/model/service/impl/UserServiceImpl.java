package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.DAOFactory;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityAlreadyExistsDAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.EntityAlreadyExistsServiceException;
import com.example.hotel.model.service.exception.IncorrectCredentialsServiceException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.util.Encryptor;

import java.security.NoSuchAlgorithmException;

public class UserServiceImpl implements UserService {
    public static final String SHA_256 = "SHA-256";

    @Override
    public User login(String login, String password) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        User user;
        try {
            user = daoFactory.getUserDAO().getByLogin(login);
            if (!Encryptor.encrypt(password, SHA_256).equals(user.getPassword())) {
                throw new IncorrectCredentialsServiceException();
            }
            return user;
        } catch (EntityNotFoundDAOException e) {
            throw new IncorrectCredentialsServiceException(e);
        } catch (DAOException | NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void register(User user) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            user.setPassword(Encryptor.encrypt(user.getPassword(), SHA_256));
            daoFactory.getUserDAO().create(user);
        } catch (EntityAlreadyExistsDAOException e) {
            throw new EntityAlreadyExistsServiceException(e);
        } catch (DAOException | NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getByPayment(int paymentId) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            return daoFactory.getUserDAO().getByPaymentId(paymentId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
