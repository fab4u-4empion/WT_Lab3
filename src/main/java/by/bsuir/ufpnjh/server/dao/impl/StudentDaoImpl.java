package by.bsuir.ufpnjh.server.dao.impl;

import by.bsuir.ufpnjh.server.dao.parser.ParserFactory;
import by.bsuir.ufpnjh.server.entity.Student;
import by.bsuir.ufpnjh.server.dao.StudentDao;
import by.bsuir.ufpnjh.server.exeptions.DaoException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {

    private final String STUDENTS_FILE_PATH = "src\\main\\resources\\Students.xml";

    @Override
    public List<Student> findAll() throws DaoException {
        try {
            return ParserFactory.newInstance().getStudentParser().takeAll(STUDENTS_FILE_PATH);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAllBySpeciality(String speciality) throws DaoException {
        try {
            return ParserFactory.newInstance().getStudentParser().takeAllBySpeciality(STUDENTS_FILE_PATH, speciality);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findByGradeBookNumber(String gradeBookNumber) throws DaoException {
        try {
            return ParserFactory.newInstance().getStudentParser().takeByGradeBookNumber(STUDENTS_FILE_PATH, gradeBookNumber);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void saveStudent(Student student) throws DaoException {
        try {
            ParserFactory.newInstance().getStudentParser().saveStudentInfo(STUDENTS_FILE_PATH, student);
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void updateStudentByGradeBookNumber(String gradeBookNumberToSearch, Student student) throws DaoException {
        try {
            ParserFactory.newInstance().getStudentParser().updateStudentInfo(STUDENTS_FILE_PATH, gradeBookNumberToSearch, student);
        } catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }
}
