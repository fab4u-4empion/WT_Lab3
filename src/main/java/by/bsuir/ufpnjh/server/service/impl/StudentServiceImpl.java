package by.bsuir.ufpnjh.server.service.impl;

import by.bsuir.ufpnjh.server.entity.Gender;
import by.bsuir.ufpnjh.server.entity.Student;
import by.bsuir.ufpnjh.server.dao.DaoFactory;
import by.bsuir.ufpnjh.server.dao.StudentDao;
import by.bsuir.ufpnjh.server.exeptions.DaoException;
import by.bsuir.ufpnjh.server.exeptions.ServiceException;
import by.bsuir.ufpnjh.server.service.StudentService;
import by.bsuir.ufpnjh.server.service.validator.Validator;
import by.bsuir.ufpnjh.server.service.validator.ValidatorFactory;

import java.util.*;

public class StudentServiceImpl implements StudentService {

    @Override
    public List<Student> findAll() throws ServiceException {
        try {
            StudentDao studentDao = DaoFactory.newInstance().getStudentDao();
            return studentDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAllBySpeciality(String speciality) throws ServiceException {
        try {
            StudentDao studentDao = DaoFactory.newInstance().getStudentDao();
            return studentDao.findAllBySpeciality(speciality);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findByGradeBookNumber(String gradeBookNumber) throws ServiceException {
        try {
            StudentDao studentDao = DaoFactory.newInstance().getStudentDao();
            return studentDao.findByGradeBookNumber(gradeBookNumber);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean saveStudent(String name, String surname, String gradeBookNumber,
                               String speciality, String gender, String stringBirthday) throws ServiceException {
        if (name == null || surname == null || stringBirthday == null
                || gender == null || gradeBookNumber == null || speciality == null) {
            return false;
        }

        Validator gradeBookValidator = ValidatorFactory.newInstance().getGradeBookValidator();
        if (!gradeBookValidator.isValid(gradeBookNumber)) {
            return false;
        }
        Date birthday = buildBirthday(stringBirthday);
        Student student = buildStudentInfo(name, surname, gradeBookNumber, speciality, Gender.valueOf(gender), birthday);

        StudentDao studentDao = DaoFactory.newInstance().getStudentDao();
        try {
            studentDao.saveStudent(student);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStudentByGradeBookNumber(String gradeBookNumberToSearch, String name, String surname,
                                                  String gradeBookNumber, String speciality,
                                                  String gender, String stringBirthday) throws ServiceException {
        if (gradeBookNumberToSearch == null || name == null || surname == null
                || stringBirthday == null || gender == null || gradeBookNumber == null || speciality == null) {
            return false;
        }

        Validator gradeBookValidator = ValidatorFactory.newInstance().getGradeBookValidator();
        if (!gradeBookValidator.isValid(gradeBookNumber)
                && gradeBookValidator.isValid(gradeBookNumberToSearch)) {
            return false;
        }
        Date birthday = buildBirthday(stringBirthday);
        Student student = buildStudentInfo(name, surname, gradeBookNumber, speciality, Gender.valueOf(gender), birthday);

        StudentDao studentDao = DaoFactory.newInstance().getStudentDao();
        try {
            studentDao.updateStudentByGradeBookNumber(gradeBookNumberToSearch, student);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private Date buildBirthday(String birthday) {
        String[] data = birthday.split("\\.");
        int day = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]) - 1;
        int year = Integer.parseInt(data[2]);
        Calendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }

    private Student buildStudentInfo(String name, String surname, String gradeBookNumber, String speciality, Gender gender, Date birthday) {
        return new Student(name, surname, gradeBookNumber, speciality, gender, birthday);
    }
}
