package com.pms.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoomDTO(@NotBlank(message = "O número do quarto não pode estar em branco.")
                      String number,
                      @NotNull(message = "O quarto precisa pertencer a uma categoria.")
                      UUID roomTypeId,
                      @NotBlank(message = "Status do quarto não informado")
                      String status) {
}
