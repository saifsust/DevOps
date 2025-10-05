package com.techview.controller;

import com.techview.model.Rider;
import com.techview.model.RiderType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = RiderController.PATH)
public class RiderController {
    static final String PATH = "/rider";

    @GetMapping
    public ResponseEntity<List<Rider>> riders() {
        return ResponseEntity
                .ok(List.of(
                        new Rider(1, "Helal", RiderType.Bicycle),
                        new Rider(2, "Austik", RiderType.Motorcycle),
                        new Rider(3, "Abrahum", RiderType.Bicycle),
                        new Rider(4, "Saiful", RiderType.Bicycle)
                ));
    }
}
