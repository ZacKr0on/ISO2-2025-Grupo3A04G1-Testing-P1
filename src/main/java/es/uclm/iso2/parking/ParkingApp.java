package es.uclm.iso2.parking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Console interface. It does not contain parking business rules.
 */
public final class ParkingApp {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    private ParkingApp() {
        // Utility class: no instances required.
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            try {
                System.out.println("=== Parking fee calculator ===");
                System.out.print("Entry date and time (dd/MM/yyyy HH:mm): ");
                LocalDateTime entry = LocalDateTime.parse(scanner.nextLine(), FORMATTER);

                System.out.print("Exit date and time (dd/MM/yyyy HH:mm): ");
                LocalDateTime exit = LocalDateTime.parse(scanner.nextLine(), FORMATTER);

                ParkingStay stay = new ParkingStay(entry, exit);
                ParkingFeeCalculator calculator = new ParkingFeeCalculator();
                BigDecimal fee = calculator.calculate(stay);

                System.out.println("Total parking fee: " + fee.toPlainString() + " €");
            } catch (DateTimeParseException exception) {
                System.out.println("Error: invalid date/time format. Use dd/MM/yyyy HH:mm.");
            } catch (IllegalArgumentException | NullPointerException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }
}
