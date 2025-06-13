package com.lottofun.domain;

import com.lottofun.enums.DrawStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "draws")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime drawDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "draw_winning_numbers", joinColumns = @JoinColumn(name = "draw_id"))
    @Column(name = "number")
    private List<Integer> winningNumbers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawStatus status;

    @OneToMany(mappedBy = "draw", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Draw draw)) return false;
        return Objects.equals(id, draw.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Draw{" +
                "id=" + id +
                ", drawDate=" + drawDate +
                ", winningNumbers=" + winningNumbers +
                ", status=" + status +
                '}';
    }
}