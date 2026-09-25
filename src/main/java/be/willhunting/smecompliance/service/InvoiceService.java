package be.willhunting.smecompliance.service;

import be.willhunting.smecompliance.dto.InvoiceSubmissionRequest;
import be.willhunting.smecompliance.dto.InvoiceSubmissionResponse;

public interface InvoiceService {

    InvoiceSubmissionResponse submitInvoice(InvoiceSubmissionRequest request);
}
