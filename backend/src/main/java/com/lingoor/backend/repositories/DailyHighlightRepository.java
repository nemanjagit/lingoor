package com.lingoor.backend.repositories;

import com.lingoor.backend.models.DailyHighlight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyHighlightRepository extends JpaRepository<DailyHighlight, Long> {
    Optional<DailyHighlight> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);
}
