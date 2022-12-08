package by.bsuir.ufpnjh.server.dao.parser.impl;

import by.bsuir.ufpnjh.server.dao.parser.XmlUserParser;
import by.bsuir.ufpnjh.server.entity.Role;
import by.bsuir.ufpnjh.server.entity.User;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class XmlUserParserImpl implements XmlUserParser {

    @Override
    public Optional<User> takeUser(String path, String login, String password) throws IOException, SAXException, ParserConfigurationException {
        File xmlUsersFile = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Optional<User> result = Optional.empty();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlUsersFile);
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("user");

        for (int i = 0; i < nodeList.getLength(); i++) {
            User user = takeUser(nodeList.item(i));
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                result = Optional.of(user);
                break;
            }
        }

        return result;
    }

    private User takeUser(Node node) {
        User user = new User();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            user.setLogin(getTagValue("login", element));
            user.setPassword(getTagValue("password", element));
            user.setRole(Role.valueOf(getTagValue("role", element)));

        }
        return user;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
