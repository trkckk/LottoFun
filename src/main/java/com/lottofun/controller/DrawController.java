package com.lottofun.controller;

import com.lottofun.dto.response.DrawResponseDto;
import com.lottofun.services.impl.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    @GetMapping("/history")
    public ResponseEntity<List<DrawResponseDto>> getDrawHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<DrawResponseDto> draws = drawService.getCompletedDraws(page, size);
        return ResponseEntity.ok(draws);
    }
}
