package com.lottofun.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lottofun.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticketNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_selected_numbers", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "number")
    private List<Integer> selectedNumbers;

    @Column(nullable = false)
    private LocalDateTime purchaseTime;

    private Integer matchCount;

    private Double prize;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_id")
    private Draw draw;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", selectedNumbers=" + selectedNumbers +
                ", purchaseTime=" + purchaseTime +
                ", matchCount=" + matchCount +
                ", prize=" + prize +
                ", status=" + status +
                '}';
    }
}