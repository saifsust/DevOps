package com.techview.services.impl;

import com.techview.model.Rider;
import com.techview.model.RiderType;
import com.techview.services.RiderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Service
public class RiderServiceImpl implements RiderService {
    private final BiFunction<List<Rider>, Stream<Rider>, List<Rider>> filterOrGetDefault =
            (riders, defaultRiders) -> riders.isEmpty() ? defaultRiders.toList() : riders;

    @Override
    public List<Rider> findBy(RiderType riderType) {
        return filterOrGetDefault.apply(
                getRiders().filter(
                        rider -> rider.ridingType().equals(riderType)
                ).toList(),
                getRiders()
        );

    }

    private Stream<Rider> getRiders() {
        return Stream.of(
                new Rider(1, "Helal", RiderType.BICYCLE),
                new Rider(2, "Austin", RiderType.MOTORCYCLE),
                new Rider(3, "Abraham", RiderType.BICYCLE),
                new Rider(4, "Saiful", RiderType.BICYCLE)
        );
    }
}
