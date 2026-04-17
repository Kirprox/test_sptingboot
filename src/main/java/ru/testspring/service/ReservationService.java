package ru.testspring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.testspring.model.Reservation;
import ru.testspring.model.ReservationEntity;
import ru.testspring.model.ReservationStatus;
import ru.testspring.repository.ReservationRepository;

import java.util.*;


@Service
public class ReservationService {
    private ReservationRepository repository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }


    public Reservation getReservationById(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id = " + id));
        return toDomainReservation(reservationEntity);
    }

    public List<Reservation> findAllReservation() {
        List<ReservationEntity> allEntities = repository.findAll();
        return allEntities.stream()
                .map(this::toDomainReservation).toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.id() != null) {
            throw new IllegalArgumentException("id should be empty");
        }
        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("status should be empty");
        }
        var entityToSave = new ReservationEntity(
                null,
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        var savedEntity = repository.save(entityToSave);
        return toDomainReservation(savedEntity);
    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        var reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id " + id));
        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("cannot modify reservation: status=" + reservationEntity.getStatus());
        }

        reservationEntity.setUserId(reservationToUpdate.userId());
        reservationEntity.setRoomId(reservationToUpdate.roomId());
        reservationEntity.setStartDate(reservationToUpdate.startDate());
        reservationEntity.setEndDate(reservationToUpdate.endDate());
        reservationEntity.setStatus(ReservationStatus.PENDING);

        var updatedReservation = repository.save(reservationEntity);

        return toDomainReservation(updatedReservation);
    }

    public void deleteReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Not found reservation by id " + id);
        }
        repository.deleteById(id);
    }


    public Reservation approveReservation(Long id) {
        var reservationEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found reservation by id " + id));

        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("cannot approve reservation: status=" + reservationEntity.getStatus());
        }

        var isConflict = isReservationConflict(reservationEntity);

        if (isConflict) {
            throw new IllegalStateException("cannot approve reservation: because of conflict");
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(reservationEntity);

        return toDomainReservation(reservationEntity);
    }

    private boolean isReservationConflict(ReservationEntity reservation) {
        var allReservations = repository.findAll();
        for (ReservationEntity existingReservation : allReservations) {
            if (reservation.getId().equals(existingReservation.getId())) {
                continue;
            }
            if (!reservation.getRoomId().equals(existingReservation.getRoomId())) {
                continue;
            }
            if (!existingReservation.getStatus().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (reservation.getStartDate().isBefore(existingReservation.getEndDate())
                    && existingReservation.getStartDate().isBefore(reservation.getEndDate())) {
                return true;

            }
        }
        return false;
    }

    private Reservation toDomainReservation(ReservationEntity reservation) {
        return new Reservation(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus()
        );
    }
}
// todo 3,29,23