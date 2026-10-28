package by.may.presentation.console;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ParseConsoleInput {

    public static Integer parseInt(String input, int min, int max) {
        try {
            int numInt = Integer.parseInt(input);
            if (numInt >= min && numInt <= max) {
                return numInt;
            }
            ConsoleOutput.error("Введите число от " + min + " до " + max);
        } catch (NumberFormatException e) {
            ConsoleOutput.error("Введённое число не является целым. Введите целое число от " + min + " до " + max );
        }
        return null;
    }

    public static Double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            ConsoleOutput.error("Введённое число не является вещественным");
        }
        return null;
    }

    public static Long parseLong (String input) {
        try {
            long id = Long.parseLong(input);
            if (id > 0) {
                return id;
            }
            ConsoleOutput.error("ID должен быть положительным. Введите целое положительное число");
        } catch (NumberFormatException e) {
            ConsoleOutput.error("Введённое число не является целым. Введите целое положительное число");
        }
        return null;
    }

    public static BigDecimal parseBigDecimal(String input) {
        try {
            BigDecimal price = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
            if (price.compareTo(new BigDecimal("0")) > 0) {
                return price;
            }
            ConsoleOutput.error("Цена должна быть положительным числом. Введите положительное (дробное) число");
        } catch (NumberFormatException e) {
            ConsoleOutput.error("Введённое значение не может быть ценой. Введите положительное (дробное) число");
        }
        return null;
    }

}