package by.bsuir.ufpnjh.server.dao.parser;

import by.bsuir.ufpnjh.server.entity.User;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

public interface XmlUserParser {

    Optional<User> takeUser(String path, String login, String password) throws IOException, SAXException, ParserConfigurationException;

}
