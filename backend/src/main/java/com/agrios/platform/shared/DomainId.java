package com.agrios.platform.shared;

import java.util.Objects;
import java.util.UUID;

public record DomainId(UUID value) {
    public DomainId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static DomainId random() {
        return new DomainId(UUID.randomUUID());
    }

    public static DomainId from(String value) {
        return new DomainId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
