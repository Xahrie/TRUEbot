package de.zahrie.trues.api.riot.xayah.types.common;

import java.io.Serial;

public class OriannaException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1737866138838972345L;

    public OriannaException(final String message) {
        super(message);
    }

    public OriannaException(final String message, final Throwable source) {
        super(message, source);
    }
}
