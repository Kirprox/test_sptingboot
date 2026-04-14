package ru.testspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.testspring.model.Reservation;
import ru.testspring.service.ReservationService;

import java.util.List;

@RestController
public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger((ReservationController.class));

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private final ReservationService reservationService;

    @GetMapping("{id}")
    public Reservation getReservationById(@PathVariable("id") Long id) {
        log.info("вызвался метод getReservationById: id=" + id);
        return reservationService.getReservationById(id);
    }

    @GetMapping()
    public List<Reservation> getAllReservations() {

        log.info("вызвался метод getAllReservations");
        return reservationService.findAllReservation();

    }
}
