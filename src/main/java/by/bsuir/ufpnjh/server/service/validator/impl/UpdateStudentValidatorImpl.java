package by.bsuir.ufpnjh.server.service.validator.impl;

import by.bsuir.ufpnjh.server.service.validator.AbstractValidator;

public class UpdateStudentValidatorImpl extends AbstractValidator {

    private static final String UPDATE_STUDENT_REGEX =
            "^((\\d{8}):([^:]+:){2}((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d):(MALE|FEMALE):(\\d{8}):([^:]+))$";

    @Override
    protected String getRegex() {
        return UPDATE_STUDENT_REGEX;
    }
}