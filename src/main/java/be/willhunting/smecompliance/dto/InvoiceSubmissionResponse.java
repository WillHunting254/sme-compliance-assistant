package be.willhunting.smecompliance.dto;

public record InvoiceSubmissionResponse(
        String invoiceNumber,
        String vatNumber,
        boolean vatValid,
        String status
) {
}
