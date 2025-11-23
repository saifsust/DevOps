package com.techview.controller;

import com.techview.model.Rider;
import com.techview.model.RiderType;
import com.techview.services.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = RiderController.PATH)
public class RiderController {
    static final String PATH = "/riders";

    @Autowired
    private RiderService riderService;

    @GetMapping
    public ResponseEntity<List<Rider>> riders(
            @RequestParam(name = "type", required = false) RiderType riderType
    ) {
        return ResponseEntity
                .ok(riderService.findBy(
                        riderType
                ));
    }
}
