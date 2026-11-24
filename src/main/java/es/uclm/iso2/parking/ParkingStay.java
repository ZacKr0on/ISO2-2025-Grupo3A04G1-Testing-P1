package es.uclm.iso2.parking;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a parking stay with its entry and exit date/time.
 */
public final class ParkingStay {
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

    public ParkingStay(LocalDateTime entryTime, LocalDateTime exitTime) {
        this.entryTime = Objects.requireNonNull(entryTime, "Entry time cannot be null");
        this.exitTime = Objects.requireNonNull(exitTime, "Exit time cannot be null");

        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be earlier than entry time");
        }
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
}
