package by.bsuir.ufpnjh.server.dao.parser.impl;

import by.bsuir.ufpnjh.server.entity.Gender;
import by.bsuir.ufpnjh.server.entity.Student;
import by.bsuir.ufpnjh.server.dao.parser.XmlStudentParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlStudentParserImpl implements XmlStudentParser {

    @Override
    public List<Student> takeAll(String path) throws IOException, SAXException, ParserConfigurationException {
        Document document = parse(path);
        NodeList nodeList = document.getElementsByTagName("student");
        List<Student> result = new LinkedList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(takeStudent(nodeList.item(i)));
        }
        return result;
    }

    @Override
    public Optional<Student> takeByGradeBookNumber(String path, String gradeBookNumber) throws IOException, SAXException, ParserConfigurationException {
        Document document = parse(path);
        NodeList nodeList = document.getElementsByTagName("student");
        Optional<Student> result = Optional.empty();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Student student = takeStudent(nodeList.item(i));
            if (gradeBookNumber.equals(student.getGradeBookNumber())) {
                result = Optional.of(student);
                break;
            }
        }
        return result;
    }

    @Override
    public List<Student> takeAllBySpeciality(String path, String speciality) throws IOException, SAXException, ParserConfigurationException {
        Document document = parse(path);
        NodeList nodeList = document.getElementsByTagName("student");
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Student student = takeStudent(nodeList.item(i));
            if (speciality.equals(student.getSpeciality())) {
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public void updateStudentInfo(String path, String gradeBookNumberToSearch, Student student) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Document document = parse(path);
        updateStudentInfo(document, gradeBookNumberToSearch, student);
        refreshFile(document, path);

    }

    @Override
    public void saveStudentInfo(String path, Student student) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Document document = parse(path);
        addStudentInfo(document, student);
        refreshFile(document, path);
    }

    private Document parse(String path) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    private Student takeStudent(Node node) {
        Student student = new Student();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            student.setName(getTagValue("name", element));
            student.setSurname(getTagValue("surname", element));
            student.setGradeBookNumber(getTagValue("grade-book-number", element));
            student.setSpeciality(getTagValue("speciality", element));
            student.setGender(Gender.valueOf(getTagValue("gender", element)));
            student.setBirthday(buildBirthday(getTagValue("birthday", element)));
        }
        return student;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    private Date buildBirthday(String birthday) {
        String[] data = birthday.split("\\.");
        int day = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]) - 1;
        int year = Integer.parseInt(data[2]);
        Calendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }

    private void refreshFile(Document document, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    private void updateStudentInfo(Document doc, String gradeBookNumberToSearch, Student student) {
        NodeList studentsInfoList = doc.getElementsByTagName("student");

        for (int i = 0; i < studentsInfoList.getLength(); i++) {
            Student currentStudent = takeStudent(studentsInfoList.item(i));
            if (currentStudent.getGradeBookNumber().equals(gradeBookNumberToSearch)) {
                Element element = (Element) studentsInfoList.item(i);

                Node name = element.getElementsByTagName("name").item(0).getFirstChild();
                name.setNodeValue(student.getName());

                Node surname = element.getElementsByTagName("surname").item(0).getFirstChild();
                surname.setNodeValue(student.getSurname());

                Node gradeBookNumber = element.getElementsByTagName("grade-book-number").item(0).getFirstChild();
                gradeBookNumber.setNodeValue(student.getGradeBookNumber());

                Node speciality = element.getElementsByTagName("speciality").item(0).getFirstChild();
                speciality.setNodeValue(student.getSpeciality());

                Node gender = element.getElementsByTagName("gender").item(0).getFirstChild();
                gender.setNodeValue(student.getGender().toString());

                SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yyyy");
                Node birthday = element.getElementsByTagName("birthday").item(0).getFirstChild();
                birthday.setNodeValue(formatForDate.format(student.getBirthday()));

                break;
            }
        }
    }

    private static void addStudentInfo(Document doc, Student student) {
        Element root = (Element) doc.getElementsByTagName("students").item(0);

        Element cratedStudentInfo = doc.createElement("student");
        root.appendChild(cratedStudentInfo);

        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(student.getName()));
        cratedStudentInfo.appendChild(nameElement);

        Element surnameElement = doc.createElement("surname");
        surnameElement.appendChild(doc.createTextNode(student.getSurname()));
        cratedStudentInfo.appendChild(surnameElement);

        Element gardeBookElement = doc.createElement("grade-book-number");
        gardeBookElement.appendChild(doc.createTextNode(student.getGradeBookNumber()));
        cratedStudentInfo.appendChild(gardeBookElement);

        Element specialtyElement = doc.createElement("speciality");
        specialtyElement.appendChild(doc.createTextNode(student.getSpeciality()));
        cratedStudentInfo.appendChild(specialtyElement);

        Element genderElement = doc.createElement("gender");
        genderElement.appendChild(doc.createTextNode(student.getGender().toString()));
        cratedStudentInfo.appendChild(genderElement);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Element birthdayElement = doc.createElement("birthday");
        birthdayElement.appendChild(doc.createTextNode(format.format(student.getBirthday())));
        cratedStudentInfo.appendChild(birthdayElement);
    }
}
