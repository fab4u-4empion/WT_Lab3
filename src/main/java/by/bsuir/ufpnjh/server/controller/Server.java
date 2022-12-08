package by.bsuir.ufpnjh.server.controller;

import by.bsuir.ufpnjh.connection.Message;
import by.bsuir.ufpnjh.scanner.ScanTool;
import by.bsuir.ufpnjh.server.entity.Role;
import by.bsuir.ufpnjh.server.entity.Student;
import by.bsuir.ufpnjh.server.entity.User;
import by.bsuir.ufpnjh.server.service.ServiceFactory;
import by.bsuir.ufpnjh.server.service.StudentService;
import by.bsuir.ufpnjh.server.service.UserService;
import by.bsuir.ufpnjh.server.service.validator.Validator;
import by.bsuir.ufpnjh.server.service.validator.ValidatorFactory;
import by.bsuir.ufpnjh.connection.MessageType;
import by.bsuir.ufpnjh.connection.Connection;
import by.bsuir.ufpnjh.server.exeptions.ServiceException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

public class Server {

    private ServerSocket serverSocket;

    private static volatile boolean isServerStart = false;

    public static void main(String[] args) {
        Server server = new Server();

        while (true) {
            System.out.println("Введите номер порта (1025-65536) для запуска сервера");
            int port = ScanTool.scanPositiveNumberInRange(1025, 65535);
            if (port == -1) {
                System.out.println("Неверный номер порта");
            } else {
                server.startServer(port);
            }
            if (isServerStart) {
                server.acceptServer();
                isServerStart = false;
            }
        }
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isServerStart = true;
            System.out.println("Сервер запущен.");
            try {
                InetAddress address = InetAddress.getLocalHost();
                System.out.println("Адрес сервера для подключения: " + address.getHostAddress() + ":" + port);
            } catch (UnknownHostException e) {
                System.out.println("Не удалось узнать адрес сервера");
            }
        } catch (Exception e) {
            System.out.println("Не удалось запустить сервер.");
        }
    }

    private void acceptServer() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            } catch (IOException e) {
                System.out.println("Ошибка подключения пользователя");
            }
        }
    }

    private static class ServerThread extends Thread {

        private final Socket socket;

        private UserService userService;

        private StudentService studentService;

        private Role role;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Подключился новый пользователь с удаленным сокетом: " + socket.getRemoteSocketAddress());
            try {
                Connection connection = new Connection(socket);
                receiveMessageFromUser(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void receiveMessageFromUser(Connection connection) {
            boolean isUserConnect = true;
            userService = ServiceFactory.newInstance().getUserService();
            studentService = ServiceFactory.newInstance().getStudentInfoService();
            while (isUserConnect) {
                try {
                    Message message = connection.receive();

                    if (message.getTypeMessage() == MessageType.LOGIN) {
                        Validator validator = ValidatorFactory.newInstance().getLoginPasswordValidator();
                        if (validator.isValid(message.getTextMessage())) {
                            if (IsLogIn(message.getTextMessage())) {
                                connection.send(new Message(MessageType.LOGIN_SUCCESS));
                            } else {
                                connection.send(new Message(MessageType.LOGIN_ERROR, "Неверный логин или пароль"));
                            }
                        } else {
                            connection.send(new Message(MessageType.LOGIN_ERROR, "Ошибка валидации"));
                        }

                    }

                    if (message.getTypeMessage() == MessageType.SELECT_ALL_STUDENTS_INFO) {
                        connection.send(new Message(MessageType.SELECT_ALL_STUDENTS_INFO_SUCCESS, takeAllStudentsInfo()));
                    }

                    if (message.getTypeMessage() == MessageType.SELECT_STUDENTS_INFO_BY_SPECIALITY) {
                        connection.send(new Message(MessageType.SELECT_STUDENTS_INFO_BY_SPECIALITY_SUCCESS,
                                takeStudentInfoBySpeciality(message.getTextMessage())));
                    }

                    if (message.getTypeMessage() == MessageType.SELECT_STUDENT_INFO_BY_GRADE_BOOK_NUMBER) {
                        Validator validator = ValidatorFactory.newInstance().getGradeBookValidator();
                        if (validator.isValid(message.getTextMessage())) {
                            connection.send(new Message(MessageType.SELECT_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_SUCCESS,
                                    takeStudentInfoByByBookGradeNumber(message.getTextMessage())));
                        } else {
                            connection.send(new Message(MessageType.SELECT_STUDENT_INFO_BY_GRADE_NUMBER_ERROR,
                                    "Ошибка валидации"));
                        }
                    }

                    if (message.getTypeMessage() == MessageType.SAVE_STUDENT_INFO) {
                        if (role == Role.ADMIN) {
                            Validator validator = ValidatorFactory.newInstance().getStudentValidator();
                            if (validator.isValid(message.getTextMessage())) {
                                connection.send(new Message(MessageType.SAVE_STUDENT_INFO_SUCCESS,
                                        saveStudentInfoB(message.getTextMessage())));
                            } else {
                                connection.send(new Message(MessageType.SAVE_STUDENT_INFO_ERROR,
                                        "Ошибка валидации"));
                            }
                        } else {
                            connection.send(new Message(MessageType.SAVE_STUDENT_INFO_ERROR,
                                    "У вас недостаточно прав для этого"));
                        }
                    }

                    if (message.getTypeMessage() == MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER) {
                        if (role == Role.ADMIN) {
                            Validator validator = ValidatorFactory.newInstance().getUpdateStudentValidator();
                            if (validator.isValid(message.getTextMessage())) {
                                connection.send(new Message(MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_SUCCESS,
                                        updateStudentInfoByBookGradeNumber(message.getTextMessage())));
                            } else {
                                connection.send(new Message(MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_ERROR,
                                        "Ошибка валидации"));
                            }
                        } else {
                            connection.send(new Message(MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_ERROR,
                                    "У вас недостаточно прав для этого"));
                        }

                    }

                    if (message.getTypeMessage() == MessageType.DISCONNECT_FROM_SERVER) {
                        connection.close();
                        isUserConnect = false;
                    }

                } catch (IOException e) {
                    System.out.println("Ошибка при приеме/отправке сообщения клиента");
                    isUserConnect = false;
                    try {
                        connection.close();
                    } catch (IOException ioException) {
                        System.out.println("Не удалось закрыть соединение");
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Класс сериализуемого объекта не найден");
                }
            }
        }

        private boolean IsLogIn(String loginPassword) {
            boolean result = false;
            String[] data = loginPassword.split(":");
            try {
                Optional<User> user = userService.login(data[0], data[1]);
                if (user.isPresent()) {
                    role = user.get().getRole();
                    result = true;
                }
            } catch (ServiceException e) {
                System.out.println("Ошибка поиска пользователя");
            }
            return result;
        }

        private String takeAllStudentsInfo() {
            String result = "Не обнаружена инвормация о студентах";
            try {
                List<Student> allStudent = studentService.findAll();
                if (!(allStudent == null)) {
                    StringBuilder temp = new StringBuilder();
                    for (Student student : allStudent) {
                        temp.append(student).append("\n");
                    }
                    result = temp.toString();
                }
            } catch (ServiceException e) {
                System.out.println("Ошибка при поиске информации о всех студентах");
            }
            return result;
        }

        private String takeStudentInfoBySpeciality(String speciality) {
            String result = "Не обнаружена инвормация о студентах";
            try {
                List<Student> studentsInfo = studentService.findAllBySpeciality(speciality);
                if (!(studentsInfo == null)) {
                    StringBuilder temp = new StringBuilder();
                    for (Student student : studentsInfo) {
                        temp.append(student).append("\n");
                    }
                    result = temp.toString();
                }
            } catch (ServiceException e) {
                System.out.println("Ошибка при поиске информации о  студентах");
            }
            return result;
        }

        private String takeStudentInfoByByBookGradeNumber(String number) {
            String result = "Не обнаружена информация о студенте";
            try {
                Optional<Student> studentsInfo = studentService.findByGradeBookNumber(number);
                if (studentsInfo.isPresent()) {
                    result = studentsInfo.get().toString();
                }
            } catch (ServiceException e) {
                System.out.println("Ошибка при поиске информации о  студенте");
            }
            return result;
        }

        private String saveStudentInfoB(String studentInfo) {
            String result = "Информация о студенте сохранена";
            String[] data = studentInfo.split(":");
            try {
                studentService.saveStudent(data[0], data[1], data[4], data[5], data[3], data[2]);
            } catch (ServiceException e) {
                System.out.println("Ошибка при сохранении информации о студенте");
            }
            return result;
        }

        private String updateStudentInfoByBookGradeNumber(String studentInfo) {
            String result = "Информация о студенте обновлена";
            String[] data = studentInfo.split(":");
            try {
                studentService.updateStudentByGradeBookNumber(data[0], data[1], data[2], data[5], data[6], data[4], data[3]);
            } catch (ServiceException e) {
                System.out.println("Ошибка при обновлении информации о студенте");
            }
            return result;
        }

    }
}
