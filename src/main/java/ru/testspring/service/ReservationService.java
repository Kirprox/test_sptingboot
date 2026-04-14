package ru.testspring.service;

import org.springframework.stereotype.Service;
import ru.testspring.model.Reservation;
import ru.testspring.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap = Map.of(
            1L, new Reservation(
                    1L,
                    100L,
                    40L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.APPROVED
            ),
            2L, new Reservation(
                    2L,
                    110L,
                    46L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.APPROVED
            ),
            3L, new Reservation(
                    3L,
                    100L,
                    40L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    ReservationStatus.APPROVED)
    );


    public Reservation getReservationById(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("Not found reservation by id " + id);
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservation() {
        return reservationMap.values().stream().toList();
    }
}
