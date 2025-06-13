package com.lottofun.repository;

import com.lottofun.domain.Draw;
import com.lottofun.enums.DrawStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawRepository extends JpaRepository<Draw, Long> {
    Optional<Draw> findFirstByStatusOrderByDrawDateAsc(DrawStatus drawStatus);
    Page<Draw> findByStatusOrderByDrawDateDesc(DrawStatus status, Pageable pageable);
}
