package be.willhunting.smecompliance.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LocalVatValidationClientTest {

    private final LocalVatValidationClient vatValidationClient = new LocalVatValidationClient();

    @Test
    void isValidReturnsTrueForBelgianVatNumberWithValidCheckDigits() {
        assertThat(vatValidationClient.isValid("BE0123456749")).isTrue();
    }

    @Test
    void isValidAllowsCommonSeparators() {
        assertThat(vatValidationClient.isValid("BE 0123.456.749")).isTrue();
    }

    @Test
    void isValidReturnsFalseForInvalidCheckDigits() {
        assertThat(vatValidationClient.isValid("BE0123456789")).isFalse();
    }

    @Test
    void isValidReturnsFalseForUnsupportedFormat() {
        assertThat(vatValidationClient.isValid("FR0123456749")).isFalse();
    }
}
