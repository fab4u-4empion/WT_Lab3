package by.bsuir.ufpnjh.server.dao.parser;

import by.bsuir.ufpnjh.server.entity.Student;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface XmlStudentParser {

    List<Student> takeAll(String path) throws IOException, SAXException, ParserConfigurationException;

    List<Student> takeAllBySpeciality(String path, String  speciality) throws IOException, SAXException, ParserConfigurationException;

    Optional<Student> takeByGradeBookNumber(String path, String gradeBookNumber) throws IOException, SAXException, ParserConfigurationException;

    void saveStudentInfo(String path, Student student) throws IOException, SAXException, ParserConfigurationException, TransformerException;

    void updateStudentInfo(String path, String gradeBookNumberToSearch, Student student) throws IOException, SAXException, ParserConfigurationException, TransformerException;

}
