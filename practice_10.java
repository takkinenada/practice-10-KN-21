package practice_10;

import java.util.Scanner;

class AuthException extends Exception {
    public AuthException(String message) { super(message); }
}

class MaxUsersReachedException extends AuthException {
    public MaxUsersReachedException(String message) { super(message); }
}

class ValidationException extends AuthException {
    public ValidationException(String message) { super(message); }
}

class UserNotFoundException extends AuthException {
    public UserNotFoundException(String message) { super(message); }
}

public class practice_10 {
    private static String[] usernames = new String[15];
    private static String[] passwords = new String[15];
    private static String[] forbiddenPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};
    private static int userCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Реєстрація (додати користувача)");
            System.out.println("2. Видалення за ім'ям");
            System.out.println("3. Вхід (виконати дію)");
            System.out.println("4. Вихід");
            System.out.print("Виберіть дію: ");

            String choice = scanner.nextLine();

            try {
                if (choice.equals("1")) {
                    registerUser(scanner);
                } else if (choice.equals("2")) {
                    deleteUser(scanner);
                } else if (choice.equals("3")) {
                    authenticate(scanner);
                } else if (choice.equals("4")) {
                    break;
                } else {
                    System.out.println("Невірний вибір.");
                }
            } catch (AuthException e) {
                System.out.println("ПОМИЛКА: " + e.getMessage());
            }
        }
    }

    private static void registerUser(Scanner scanner) throws AuthException {
        if (userCount >= 15) {
            throw new MaxUsersReachedException("Більше 15 користувачів не можна додати.");
        }

        System.out.print("Ім'я користувача: ");
        String name = scanner.nextLine();
        validateUsername(name);

        System.out.print("Пароль: ");
        String pass = scanner.nextLine();
        validatePassword(pass);

        for (int i = 0; i < 15; i++) {
            if (usernames[i] == null) {
                usernames[i] = name;
                passwords[i] = pass;
                userCount++;
                System.out.println("Користувача додано.");
                return;
            }
        }
    }

    private static void deleteUser(Scanner scanner) throws UserNotFoundException {
        System.out.print("Ім'я для видалення: ");
        String name = scanner.nextLine();

        for (int i = 0; i < 15; i++) {
            if (usernames[i] != null && usernames[i].equals(name)) {
                usernames[i] = null;
                passwords[i] = null;
                userCount--;
                System.out.println("Успішно видалено.");
                return;
            }
        }
        throw new UserNotFoundException("Такого користувача немає.");
    }

    private static void authenticate(Scanner scanner) throws AuthException {
        System.out.print("Логін: ");
        String name = scanner.nextLine();
        System.out.print("Пароль: ");
        String pass = scanner.nextLine();

        for (int i = 0; i < 15; i++) {
            if (usernames[i] != null && usernames[i].equals(name)) {
                if (passwords[i].equals(pass)) {
                    System.out.println("Користувача було аутентифіковано.");
                    return;
                } else {
                    throw new ValidationException("Пароль не відповідає вказаному при реєстрації.");
                }
            }
        }
        throw new UserNotFoundException("Користувача не існує.");
    }

    private static void validateUsername(String name) throws ValidationException {
        if (name.length() < 5) throw new ValidationException("Не менше 5 символів.");
        if (hasSpace(name)) throw new ValidationException("Не має мати пробіли.");
    }

    private static void validatePassword(String pass) throws ValidationException {
        if (pass.length() < 10) throw new ValidationException("Довжина не менше 10.");
        if (hasSpace(pass)) throw new ValidationException("Пароль не має містити пробілів.");

        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specials = "!@#$%^&*()-_=+[]{}<>.,;'';/№";

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isDigit(c)) hasDigit = true;
            if (specials.indexOf(c) >= 0) hasSpecial = true;

            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c) || specials.indexOf(c) >= 0)) {
                throw new ValidationException("Тільки латинські символи, цифри та спецсимволи.");
            }
        }

        if (!hasDigit) throw new ValidationException("Має бути хоча б одна цифра.");
        if (!hasSpecial) throw new ValidationException("Має бути хоча б 1 спецсимвол.");

        for (String forbidden : forbiddenPasswords) {
            if (pass.toLowerCase().contains(forbidden)) {
                throw new ValidationException("Пароль містить заборонене слово.");
            }
        }
    }

    private static boolean hasSpace(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') return true;
        }
        return false;
    }
}