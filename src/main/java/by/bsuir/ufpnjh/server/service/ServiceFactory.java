package by.bsuir.ufpnjh.server.service;

import by.bsuir.ufpnjh.server.service.impl.StudentServiceImpl;
import by.bsuir.ufpnjh.server.service.impl.UserServiceImpl;

public class ServiceFactory {

    private static volatile ServiceFactory INSTANCE;

    private final UserService userService = new UserServiceImpl();

    private final StudentService studentService = new StudentServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory newInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceFactory.class) {
                if (INSTANCE == null) INSTANCE = new ServiceFactory();
            }
        }
        return INSTANCE;
    }

    public UserService getUserService() {
        return userService;
    }

    public StudentService getStudentInfoService() {
        return studentService;
    }

}
