package com.lottofun.dto.response;

import com.lottofun.enums.DrawStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DrawResponseDto {

    private Long drawId;
    private LocalDateTime drawDate;
    private DrawStatus status;
    private List<Integer> winningNumbers;
}
