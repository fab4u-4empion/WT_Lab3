package by.bsuir.ufpnjh.server.dao.parser;

import by.bsuir.ufpnjh.server.dao.parser.impl.XmlStudentParserImpl;
import by.bsuir.ufpnjh.server.dao.parser.impl.XmlUserParserImpl;

public class ParserFactory {

    private static volatile ParserFactory INSTANCE;

    private final XmlUserParser userParser = new XmlUserParserImpl();

    private final XmlStudentParser studentParser = new XmlStudentParserImpl();

    public ParserFactory() {
    }

    public static ParserFactory newInstance() {
        if (INSTANCE == null) {
            synchronized (ParserFactory.class) {
                if (INSTANCE == null) INSTANCE = new ParserFactory();
            }
        }
        return INSTANCE;
    }

    public XmlUserParser getUserParser() {
        return userParser;
    }

    public XmlStudentParser getStudentParser() {
        return studentParser;
    }

}
