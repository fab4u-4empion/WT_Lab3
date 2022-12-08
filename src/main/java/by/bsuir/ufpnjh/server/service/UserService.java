package by.bsuir.ufpnjh.server.service;

import by.bsuir.ufpnjh.server.entity.User;
import by.bsuir.ufpnjh.server.exeptions.ServiceException;

import java.util.Optional;

public interface UserService {

    Optional<User> login(String login, String password) throws ServiceException;

}
