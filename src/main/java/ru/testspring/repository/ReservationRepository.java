package ru.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.testspring.model.ReservationEntity;

//@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

}
