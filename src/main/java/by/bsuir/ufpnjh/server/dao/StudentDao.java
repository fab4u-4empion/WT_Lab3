package by.bsuir.ufpnjh.server.dao;

import by.bsuir.ufpnjh.server.entity.Student;
import by.bsuir.ufpnjh.server.exeptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    List<Student> findAll() throws DaoException;

    List<Student> findAllBySpeciality(String gradeBookNumber) throws DaoException;

    Optional<Student> findByGradeBookNumber(String gradeBookNumber) throws DaoException;

    void saveStudent(Student student) throws DaoException;

    void updateStudentByGradeBookNumber(String gradeBookNumber, Student student) throws DaoException;

}
