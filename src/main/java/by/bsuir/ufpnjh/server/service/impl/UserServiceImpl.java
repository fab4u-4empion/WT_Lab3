package by.bsuir.ufpnjh.server.service.impl;

import by.bsuir.ufpnjh.server.dao.DaoFactory;
import by.bsuir.ufpnjh.server.dao.UserDao;
import by.bsuir.ufpnjh.server.entity.User;
import by.bsuir.ufpnjh.server.exeptions.DaoException;
import by.bsuir.ufpnjh.server.exeptions.ServiceException;
import by.bsuir.ufpnjh.server.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        if (login == null || password == null) {
            return Optional.empty();
        }

        try {
            UserDao userDao = DaoFactory.newInstance().getUserDao();
            return userDao.findByLoginAndPassword(login, password);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
