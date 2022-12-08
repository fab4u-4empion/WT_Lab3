package by.bsuir.ufpnjh.server.service.validator.impl;

import by.bsuir.ufpnjh.server.service.validator.AbstractValidator;

public class GradeBookValidatorImpl extends AbstractValidator {

    private static final String GRADE_BOOK_NUMBER_REGEX = "^\\d{8}$";

    @Override
    protected String getRegex() {
        return GRADE_BOOK_NUMBER_REGEX;
    }
}
