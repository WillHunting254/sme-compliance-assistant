package be.willhunting.smecompliance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.willhunting.smecompliance.client.VatValidationClient;
import be.willhunting.smecompliance.dto.InvoiceSubmissionRequest;
import be.willhunting.smecompliance.dto.InvoiceSubmissionResponse;
import be.willhunting.smecompliance.exception.InvalidVatNumberException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private VatValidationClient vatValidationClient;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    void submitInvoiceReturnsAcceptedResponseWhenVatNumberIsValid() {
        InvoiceSubmissionRequest request = validRequest("BE0123456749");
        when(vatValidationClient.isValid("BE0123456749")).thenReturn(true);

        InvoiceSubmissionResponse response = invoiceService.submitInvoice(request);

        assertThat(response.invoiceNumber()).isEqualTo("INV-2026-0001");
        assertThat(response.vatNumber()).isEqualTo("BE0123456749");
        assertThat(response.vatValid()).isTrue();
        assertThat(response.status()).isEqualTo("ACCEPTED");
        verify(vatValidationClient).isValid("BE0123456749");
    }

    @Test
    void submitInvoiceThrowsInvalidVatNumberExceptionWhenVatNumberIsInvalid() {
        InvoiceSubmissionRequest request = validRequest("BE0123456789");
        when(vatValidationClient.isValid("BE0123456789")).thenReturn(false);

        assertThatThrownBy(() -> invoiceService.submitInvoice(request))
                .isInstanceOf(InvalidVatNumberException.class)
                .hasMessage("VAT number is invalid: BE0123456789");
    }

    private InvoiceSubmissionRequest validRequest(String vatNumber) {
        return new InvoiceSubmissionRequest(
                "INV-2026-0001",
                vatNumber,
                LocalDate.of(2026, 9, 23),
                new BigDecimal("125.50")
        );
    }
}
