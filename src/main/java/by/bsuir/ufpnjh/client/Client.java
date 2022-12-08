package by.bsuir.ufpnjh.client;

import by.bsuir.ufpnjh.scanner.ScanTool;
import by.bsuir.ufpnjh.connection.Connection;
import by.bsuir.ufpnjh.connection.Message;
import by.bsuir.ufpnjh.connection.MessageType;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Connection connection;

    private volatile boolean isConnect = false;

    private volatile boolean isLogin = false;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (true) {
            if (!client.isConnect()) {
                System.out.println("Введите номер команды");
                System.out.println("1) Подключиться к серверу");
                System.out.println("2) Завершить выполнение программы");
                int result = ScanTool.scanPositiveNumberInRange(1, 2);
                switch (result) {
                    case 1:
                        client.connectToServer();
                        break;
                    case 2:
                        client.finishedProgram();
                        break;
                }
            }
            if (client.isConnect()) {
                client.logIn();
            }
        }
    }

    protected void connectToServer() {
        System.out.println("Введите адрес сервера в формате ip:port");
        String addressServer = ScanTool.scanAddressServer();
        if (addressServer.equals("-1")) {
            System.out.println("Вы ввели данные в неправильном формате");
        } else {
            String[] data = addressServer.split(":");
            String ip = data[0];
            int port = Integer.parseInt(data[1]);
            if (!isConnect) {
                try {
                    Socket socket = new Socket(ip, port);
                    connection = new Connection(socket);
                    isConnect = true;
                    System.out.println("Вы подключились к серверу.");

                } catch (Exception e) {
                    System.out.println("Произошла ошибка! Возможно Вы ввели не верный адрес сервера или порт. Попробуйте еще раз");
                }
            }
        }

    }


    protected void finishedProgram() {
        System.exit(0);
    }


    protected void logIn() {
        boolean isInputCorrect = false;
        String loginPassword = null;


        while (!isInputCorrect) {
            System.out.println("Введите логин и пароль в указанном формате: логин:пароль ");
            String tempLoginPassword = ScanTool.scanLoginPassword();

            if (!tempLoginPassword.equals("-1")) {
                String[] data = tempLoginPassword.split(":");
                String password = DigestUtils.sha1Hex(data[1]);
                loginPassword = data[0] + ":" + password;
                isInputCorrect = true;
            } else {
                System.out.println("Неправильный  формат ввода");
            }
        }

        try {
            connection.send(new Message(MessageType.LOGIN, loginPassword));
            this.receiveMessageFromServer();
        } catch (IOException e) {
            System.out.println("Не удалось отправить запрос на вход");
        }

    }

    protected void receiveMessageFromServer() {

        while (isConnect) {
            try {
                Message message = connection.receive();

                if (message.getTypeMessage() == MessageType.LOGIN_SUCCESS) {
                    System.out.println("Вход выполнен успешно");
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.SELECT_ALL_STUDENTS_INFO_SUCCESS) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.SELECT_STUDENTS_INFO_BY_SPECIALITY_SUCCESS) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.SELECT_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_SUCCESS) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.SAVE_STUDENT_INFO_SUCCESS) {
                    System.out.println("Информация о студенте успешно сохранена");
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_SUCCESS) {
                    System.out.println("Информация о студенте успешно изменина");
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.LOGIN_ERROR) {
                    System.out.println(message.getTextMessage());
                    logIn();
                }

                if (message.getTypeMessage() == MessageType.SELECT_STUDENT_INFO_BY_GRADE_NUMBER_ERROR) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.SAVE_STUDENT_INFO_ERROR) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }

                if (message.getTypeMessage() == MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER_ERROR) {
                    System.out.println(message.getTextMessage());
                    makeChoice();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при приеме/отправке сообщения от сервера.");
                isConnect = false;
                try {
                    connection.close();
                } catch (IOException ioException) {
                    System.out.println("Не удалось закрыть соединение");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Что-то пошло не так");
            }
        }
    }

    protected void makeChoice() {
        System.out.println("Введите номер команды");
        System.out.println("1) Получить информацию о всех студентах");
        System.out.println("2) Получить инвормацию о студенте по номеру студенческого");
        System.out.println("3) Получить информацию о всех студентах определённой специальности");
        System.out.println("4) Сохранить информацию о студенте");
        System.out.println("5) Изменить инвормацию о студенте по номеру студенческого");
        System.out.println("6) Отключиться от сервера");
        int result = -1;
        while (result == -1) {
            result = ScanTool.scanPositiveNumberInRange(1, 6);
            if (result == -1) {
                System.out.println("Неверный номер команды. Введите занаво");
            }
        }

        try {
            switch (result) {
                case 1:
                    connection.send(new Message(MessageType.SELECT_ALL_STUDENTS_INFO));
                    break;
                case 2:
                    connection.send(new Message(MessageType.SELECT_STUDENT_INFO_BY_GRADE_BOOK_NUMBER, takeBookNumber()));
                    break;
                case 3:
                    connection.send(new Message(MessageType.SELECT_STUDENTS_INFO_BY_SPECIALITY, takeSpeciality()));
                    break;
                case 4:
                    connection.send(new Message(MessageType.SAVE_STUDENT_INFO, takeStudentInfo()));
                    break;
                case 5:
                    connection.send(new Message(MessageType.UPDATE_STUDENT_INFO_BY_GRADE_BOOK_NUMBER, takeStudentInfoForUpdate()));
                    break;
                case 6:
                    disconnectFromServer();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveMessageFromServer();
    }

    protected String takeBookNumber() {
        System.out.println("Введите номер зачётки ( 8 цифр)");
        String result = "-1";
        while (result.equals("-1")) {
            result = ScanTool.scanBookNumber();
            if (result.equals("-1")) {
                System.out.println("Неверный номер зачётки. Введите занаво");
            }
        }
        return result;
    }

    protected String takeStudentInfo() {
        System.out.println("Введите информацию о студенте в следуещем формате");
        System.out.println("имя:фамилия:день.месяц.год рождения:пол:номер зачётки:специальность");
        String result = "-1";
        while (result.equals("-1")) {
            result = ScanTool.scanStudentInfo();
            if (result.equals("-1")) {
                System.out.println("Неверная информация о студенте. Введите занаво");
            }
        }
        return result;
    }

    protected String takeStudentInfoForUpdate() {
        System.out.println("Введите информацию о студенте в следуещем формате");
        System.out.println("номер зачётки для поиска:имя:фамилия:день.месяц.год рождения:пол:номер зачётки:специальность");
        String result = "-1";
        while (result.equals("-1")) {
            result = ScanTool.scanUpdateStudentInfo();
            if (result.equals("-1")) {
                System.out.println("Неверная информация о студенте. Введите занаво");
            }
        }
        return result;
    }

    protected String takeSpeciality() {
        System.out.println("Введите специальность");
        String result = "-1";
        while (result.equals("-1")) {
            result = ScanTool.scanSpeciality();
            if (result.equals("-1")) {
                System.out.println("Неверная специальность. Введите занаво");
            }
        }
        return result;
    }

    protected void disconnectFromServer() {

        if (isConnect) {
            try {
                connection.send(new Message(MessageType.DISCONNECT_FROM_SERVER));
            } catch (IOException e) {
                System.out.println("Ошибка передачи запроса на отключение на сервер");
            }
            isConnect = false;
        }
    }

}
