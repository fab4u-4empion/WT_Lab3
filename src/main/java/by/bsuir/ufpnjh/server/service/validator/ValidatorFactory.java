package by.bsuir.ufpnjh.server.service.validator;

import by.bsuir.ufpnjh.server.service.validator.impl.GradeBookValidatorImpl;
import by.bsuir.ufpnjh.server.service.validator.impl.LoginPasswordValidatorImpl;
import by.bsuir.ufpnjh.server.service.validator.impl.StudentValidatorImpl;
import by.bsuir.ufpnjh.server.service.validator.impl.UpdateStudentValidatorImpl;

public class ValidatorFactory {

    private static volatile ValidatorFactory INSTANCE;

    private final Validator gradeBookValidator = new GradeBookValidatorImpl();

    private final Validator loginPasswordValidator = new LoginPasswordValidatorImpl();

    private final Validator studentValidator = new StudentValidatorImpl();

    private final Validator updateStudentValidator = new UpdateStudentValidatorImpl();

    private ValidatorFactory() {
    }

    public static ValidatorFactory newInstance() {
        if (INSTANCE == null) {
            synchronized (ValidatorFactory.class) {
                if (INSTANCE == null) INSTANCE = new ValidatorFactory();
            }
        }
        return INSTANCE;
    }

    public Validator getGradeBookValidator() {
        return gradeBookValidator;
    }

    public Validator getStudentValidator() {
        return studentValidator;
    }

    public Validator getUpdateStudentValidator() {
        return updateStudentValidator;
    }

    public Validator getLoginPasswordValidator() {
        return loginPasswordValidator;
    }
}
