package be.willhunting.smecompliance.client;

import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class LocalVatValidationClient implements VatValidationClient {

    @Override
    public boolean isValid(String vatNumber) {
        if (vatNumber == null) {
            return false;
        }

        String normalizedVatNumber = vatNumber.replaceAll("[.\\s-]", "").toUpperCase(Locale.ROOT);
        if (!normalizedVatNumber.matches("BE0\\d{9}")) {
            return false;
        }

        // TODO: confirm rule with Belgian VAT/VIES source before relying on this outside local validation.
        long baseNumber = Long.parseLong(normalizedVatNumber.substring(2, 10));
        int checkDigits = Integer.parseInt(normalizedVatNumber.substring(10, 12));
        int expectedCheckDigits = 97 - (int) (baseNumber % 97);

        return checkDigits == expectedCheckDigits;
    }
}
