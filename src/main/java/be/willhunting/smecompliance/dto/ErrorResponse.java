package be.willhunting.smecompliance.dto;

public record ErrorResponse(
        String code,
        String message
) {
}
