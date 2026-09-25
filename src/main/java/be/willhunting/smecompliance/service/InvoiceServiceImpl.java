package be.willhunting.smecompliance.service;

import be.willhunting.smecompliance.client.VatValidationClient;
import be.willhunting.smecompliance.dto.InvoiceSubmissionRequest;
import be.willhunting.smecompliance.dto.InvoiceSubmissionResponse;
import be.willhunting.smecompliance.exception.InvalidVatNumberException;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final VatValidationClient vatValidationClient;

    public InvoiceServiceImpl(VatValidationClient vatValidationClient) {
        this.vatValidationClient = vatValidationClient;
    }

    @Override
    public InvoiceSubmissionResponse submitInvoice(InvoiceSubmissionRequest request) {
        if (!vatValidationClient.isValid(request.vatNumber())) {
            throw new InvalidVatNumberException(request.vatNumber());
        }

        return new InvoiceSubmissionResponse(
                request.invoiceNumber(),
                request.vatNumber(),
                true,
                "ACCEPTED"
        );
    }
}
