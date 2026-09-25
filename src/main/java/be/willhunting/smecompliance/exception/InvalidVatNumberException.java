package be.willhunting.smecompliance.exception;

public class InvalidVatNumberException extends RuntimeException {

    public InvalidVatNumberException(String vatNumber) {
        super("VAT number is invalid: " + vatNumber);
    }
}
