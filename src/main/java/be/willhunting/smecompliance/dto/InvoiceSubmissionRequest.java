package be.willhunting.smecompliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceSubmissionRequest(
        @NotBlank String invoiceNumber,
        @NotBlank String vatNumber,
        @NotNull LocalDate issueDate,
        @NotNull @Positive BigDecimal totalAmount
) {
}
