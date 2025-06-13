package com.lottofun.repository;

import com.lottofun.domain.Draw;
import com.lottofun.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDraw(Draw draw);
}