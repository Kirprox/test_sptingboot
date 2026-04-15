package ru.testspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.testspring.model.Reservation;
import ru.testspring.service.ReservationService;

import java.util.List;
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger((ReservationController.class));

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private final ReservationService reservationService;

    @GetMapping("{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        log.info("вызвался метод getReservationById: id=" + id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("вызвался метод getAllReservations");
        return ResponseEntity.ok(reservationService.findAllReservation());
    }

    @PostMapping()
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservationToCreate) {
        log.info("Called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("test-header", "123")
                .body(reservationService.createReservation(reservationToCreate));
    }
} //todo "1^47^56"
