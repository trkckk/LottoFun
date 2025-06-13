package com.lottofun.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketRequest {

    @NotNull(message = "Seçilen numaralar boş olamaz")
    @Size(min = 5, max = 5, message = "Tam olarak 5 sayı seçilmelidir")
    private List<Integer> selectedNumbers;

    public TicketRequest(List<Integer> selectedNumbers) { // Test için eklendi..
        this.selectedNumbers = selectedNumbers;
    }
}
