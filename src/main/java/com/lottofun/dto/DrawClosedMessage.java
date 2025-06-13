package com.lottofun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DrawClosedMessage {
    private Long drawId;
    private LocalDateTime closedAt;
    private List<Integer> winningNumbers;
}
