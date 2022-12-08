package by.bsuir.ufpnjh.server.dao;

import by.bsuir.ufpnjh.server.entity.User;
import by.bsuir.ufpnjh.server.exeptions.DaoException;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByLoginAndPassword(String login, String password) throws DaoException;

}
