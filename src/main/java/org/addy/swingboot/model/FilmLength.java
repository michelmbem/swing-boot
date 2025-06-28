package org.addy.swingboot.model;

import org.springframework.lang.NonNull;

public record FilmLength(int minutes) implements Comparable<FilmLength> {
    @Override
    public @NonNull String toString() {
        var sb = new StringBuilder();
        int length = minutes;
        boolean hasHours = false;

        if (length >= 60) {
            hasHours = true;
            sb.append(length / 60).append(" h");
            length %= 60;
        }

        if (hasHours) sb.append(' ');
        sb.append(String.format("%02d", length)).append(" min");

        return sb.toString();
    }

    @Override
    public int compareTo(@NonNull FilmLength other) {
        return minutes - other.minutes;
    }
}
