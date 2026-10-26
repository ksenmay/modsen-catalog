package by.may.presentation.console;

import by.may.entity.Role;
import by.may.service.UserSortField;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

@Component
public class ConsoleInput {

    private final Scanner scanner = new Scanner(System.in);

    public String readWhileNotBlank(String message) {
        while (true) {
            String input = readLine(message);
            if (!input.isEmpty()) {
                return input;
            }
            ConsoleOutput.error("Ввод не может быть пустым");
        }
    }

    public int readInt(String message, int min, int max) {
        while (true) {
            String input = readLine(message);
            try {
                int numInt = Integer.parseInt(input);
                if (numInt >= min && numInt <= max) {
                    return numInt;
                }
                ConsoleOutput.error("Введите число от " + min + " до " + max);
            } catch (NumberFormatException e) {
                ConsoleOutput.error("Введённое число не является целым. Введите целое число от " + min + " до " + max );
            }
        }
    }

    public Long readLong(String message) {
        while (true) {
            String input = readLine(message);
            try {
                long id = Long.parseLong(input);
                if (id > 0) {
                    return id;
                }
                ConsoleOutput.error("ID должен быть положительным. Введите целое положительное число");
            } catch (NumberFormatException e) {
                ConsoleOutput.error("Введённое число не является целым. Введите целое положительное число");
            }
        }
    }

    public BigDecimal readPrice(String message) {
        while (true) {
            String input = readLine(message).replace(",", ".");
            try {
                BigDecimal price = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
                if (price.compareTo(new BigDecimal("0")) > 0) {
                    return price;
                }
                ConsoleOutput.error("Цена должна быть положительным числом. Введите положительное (дробное) число");
            } catch (NumberFormatException e) {
                ConsoleOutput.error("Введённое значение не может быть ценой. Введите положительное (дробное) число");
            }
        }
    }

    public boolean isConfirm(String message) {
        ConsoleOutput.info("В случае согласия введите y (д), иначе - n (н)");
        while (true) {
            String input = readLine(message).toLowerCase();
            switch (input) {
                case "y", "д" -> {
                    return true;
                }
                case "n", "н" -> {
                    return false;
                }
                default -> ConsoleOutput.error("Введите y (д) или n (н)");
            }
        }
    }

    public Role readRole(String message) {
        while (true) {
            String input = readLine(message).toLowerCase();
            switch (input) {
                case "admin" -> {
                    return Role.ADMIN;
                }
                case "manager" -> {
                    return Role.MANAGER;
                }
                case "customer" -> {
                    return Role.CUSTOMER;
                }
                default -> ConsoleOutput.error("Неверная роль. Роли: ADMIN, MANAGER, CUSTOMER");
            }
        }

    }

    public UserSortField readUserSortField(String message) {
        int choice = readInt(message, 1, 2);
        if (choice == 1) {
            return UserSortField.USERNAME;
        } else {
            return UserSortField.CREATED_AT;
        }
    }

    public String readOptional(String message) {
        String input = readLine(message);
        if (input.isEmpty()) {
            return null;
        }
        return input;
    }

    private String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

}
