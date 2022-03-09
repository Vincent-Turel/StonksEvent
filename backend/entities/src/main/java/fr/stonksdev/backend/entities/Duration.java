package fr.stonksdev.backend.entities;

import java.util.Objects;

/**
 * A simpler version of the standard Duration class.
 * <p>
 * We want to address two points here:
 * <ul>
 *     <li>
 *         we only handle minute-based durations, we don't need any fancy
 *         precision here,
 *     </li>
 *     <li>
 *         we only handle positive durations,
 *     </li>
 *     <li>
 *         we do need to check for equality, hash and compare properly two
 *         durations.
 *     </li>
 * </ul>
 */
public class Duration implements Comparable<Duration> {
    // Invariant: must be <= 0.
    private final int minutes;

    public static Duration ofMinutes(int minutes) {
        return new Duration(minutes);
    }

    /**
     * @return The number of minutes represented by the duration.
     */
    public int asMinutes() {
        // minutes is a primitive type, hence it is always passed by value. As
        // such, it can't be modified from the outside.
        return this.minutes;
    }

    /**
     * @return The (floored) number of hours represented by the duration.
     */
    public int asHours() {
        return this.minutes / 60;
    }

    private Duration(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Duration can not be negative: " + minutes + "min.");
        }

        this.minutes = minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration d = (Duration) o;
        return asMinutes() == d.asMinutes();
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }

    @Override
    public int compareTo(Duration d) {
        return Integer.compare(this.asMinutes(), d.asMinutes());
    }
}
