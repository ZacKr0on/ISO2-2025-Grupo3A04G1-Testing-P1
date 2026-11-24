package es.uclm.iso2.parking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Calculates the fee for a parking stay.
 *
 * Assumptions:
 * - The first 30 minutes of the whole stay are free.
 * - The rate is 0.05 euros per subsequent minute.
 * - The 20-euro limit applies separately to each calendar day.
 * - Time is counted in complete minutes (the console accepts minute precision).
 */
public final class ParkingFeeCalculator {
    private static final long FREE_MINUTES = 30;
    private static final BigDecimal RATE_PER_MINUTE = new BigDecimal("0.05");
    private static final BigDecimal DAILY_LIMIT = new BigDecimal("20.00");

    public BigDecimal calculate(ParkingStay stay) {
        Objects.requireNonNull(stay, "Parking stay cannot be null");

        LocalDateTime current = stay.getEntryTime();
        LocalDateTime exit = stay.getExitTime();

        long freeMinutesRemaining = FREE_MINUTES;
        BigDecimal total = BigDecimal.ZERO;

        while (current.isBefore(exit)) {
            LocalDateTime nextDay = current.toLocalDate().plusDays(1).atStartOfDay();
            LocalDateTime segmentEnd = exit.isBefore(nextDay) ? exit : nextDay;

            long segmentMinutes = Duration.between(current, segmentEnd).toMinutes();
            long freeMinutes = Math.min(segmentMinutes, freeMinutesRemaining);
            freeMinutesRemaining -= freeMinutes;

            long chargeableMinutes = segmentMinutes - freeMinutes;
            BigDecimal dailyAmount = RATE_PER_MINUTE
                    .multiply(BigDecimal.valueOf(chargeableMinutes));

            if (dailyAmount.compareTo(DAILY_LIMIT) > 0) {
                dailyAmount = DAILY_LIMIT;
            }

            total = total.add(dailyAmount);
            current = segmentEnd;
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
