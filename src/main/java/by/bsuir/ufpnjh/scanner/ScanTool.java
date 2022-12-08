package by.bsuir.ufpnjh.scanner;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanTool {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String NATURAL_NUMBER_REGEX = "^(([1-9][0-9]*)|([1-9]))$";

    private static final String ADDRESS_SERVER_REGEX =
            "^(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]\\d*))$";

    private static final String LOGIN_PASSWORD_REGEX = "^[^:]+:[^:]+$";

    private static final String STUDENT_REGEX =
            "^(([^:]+:){2}((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d):(MALE|FEMALE):(\\d{8}):([^:]+))$";

    private static final String GRADE_BOOK_NUMBER_REGEX = "^\\d{8}$";

    private static final String UPDATE_STUDENT_REGEX =
            "^((\\d{8}):([^:]+:){2}((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d):(MALE|FEMALE):(\\d{8}):([^:]+))$";

    public static int scanPositiveNumberInRange(int start, int end) {
        String str = scanner.nextLine();
        int result = -1;
        if (isNaturalNumberValid(str)) {
            int tempResult = Integer.parseInt(str);
            if (isNumberInRange(tempResult, start, end)) {
                result = tempResult;
            }
        }
        return result;
    }

    public static String scanAddressServer() {
        String str = scanner.nextLine();
        String result = "-1";
        if (isAddressServerValid(str)) {
            result = str;
        }
        return result;
    }

    public static String scanLoginPassword() {
        String str = scanner.nextLine();
        String result = "-1";
        if (isLoginPasswordValid(str)) {
            result = str;
        }
        return result;
    }

    public static String scanBookNumber() {
        String str = scanner.nextLine();
        String result = "-1";
        if (isBookNumberValid(str)) {
            result = str;
        }
        return result;
    }

    public static String scanStudentInfo() {
        String str = scanner.nextLine();
        String result = "-1";
        if (isStudentValid(str)) {
            result = str;
        }
        return result;
    }

    public static String scanUpdateStudentInfo() {
        String str = scanner.nextLine();
        String result = "-1";
        if (isUpdateStudentValid(str)) {
            result = str;
        }
        return result;
    }

    public static String scanSpeciality() {
        String str = scanner.nextLine();
        String result = "-1";
        if (!str.isEmpty()) {
            result = str;
        }
        return result;
    }


    private static boolean isNaturalNumberValid(String number) {
        Pattern pattern = Pattern.compile(NATURAL_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }

    private static boolean isAddressServerValid(String addressServer) {
        Pattern pattern = Pattern.compile(ADDRESS_SERVER_REGEX);
        Matcher matcher = pattern.matcher(addressServer);
        return matcher.find();
    }

    private static boolean isNumberInRange(int number, int start, int end) {
        return number >= start && number <= end;
    }

    private static boolean isLoginPasswordValid(String loginPassword) {
        Pattern pattern = Pattern.compile(LOGIN_PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(loginPassword);
        return matcher.find();
    }

    private static boolean isStudentValid(String student) {
        Pattern pattern = Pattern.compile(STUDENT_REGEX);
        Matcher matcher = pattern.matcher(student);
        return matcher.find();
    }

    private static boolean isUpdateStudentValid(String updateStudent) {
        Pattern pattern = Pattern.compile(UPDATE_STUDENT_REGEX);
        Matcher matcher = pattern.matcher(updateStudent);
        return matcher.find();
    }

    private static boolean isBookNumberValid(String bookNumber) {
        Pattern pattern = Pattern.compile(GRADE_BOOK_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(bookNumber);
        return matcher.find();
    }
}
