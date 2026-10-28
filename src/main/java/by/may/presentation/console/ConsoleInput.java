package by.may.presentation.console;

import by.may.entity.Role;
import by.may.service.ProductSortField;
import by.may.service.UserSortField;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
            Integer num = ParseConsoleInput.parseInt(input, min, max);
            if (num != null) {
                return num;
            }
        }
    }

    public Double readOptionalRating(String message) {
        while (true) {
            String input = readLine(message);
            if (input.isEmpty()) {
                return null;
            }
            Double rating = ParseConsoleInput.parseDouble(input);
            if (rating != null) {
                return rating;
            }
        }
    }

    public Long readLong(String message) {
        while (true) {
            String input = readLine(message);
            Long id = ParseConsoleInput.parseLong(input);
            if (id != null) {
                return id;
            }
        }
    }

    public Long readOptionalLong(String message) {
        while (true) {
            String input = readLine(message);
            if (input.isEmpty()) {
                return null;
            }
            Long id = ParseConsoleInput.parseLong(input);
            if (id != null) {
                return id;
            }
        }
    }

    public BigDecimal readPrice(String message) {
        while (true) {
            String input = readLine(message).replace(",", ".");
            BigDecimal price = ParseConsoleInput.parseBigDecimal(input);
            if (price != null) {
                return price;
            }
        }
    }

    public BigDecimal readOptionalPrice(String message) {
        while (true) {
            String input = readLine(message).replace(",", ".");
            if (input.isEmpty()) {
                return null;
            }
            BigDecimal price = ParseConsoleInput.parseBigDecimal(input);
            if (price != null) {
                return price;
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

    public ProductSortField readProductSortField(String message) {
        int choice = readInt(message, 1, 3);
        if (choice == 1) {
            return ProductSortField.PRICE;
        } else if (choice == 2) {
            return ProductSortField.AVERAGE_RATING;
        } else {
            return ProductSortField.CREATED_AT;
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