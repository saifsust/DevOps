package com.techview.model;

public record Rider(
        Integer riderId,
        String name,
        RiderType ridingType
) {
}
