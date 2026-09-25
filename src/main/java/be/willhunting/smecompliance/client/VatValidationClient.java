package be.willhunting.smecompliance.client;

public interface VatValidationClient {

    boolean isValid(String vatNumber);
}
