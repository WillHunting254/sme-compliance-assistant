package be.willhunting.smecompliance.controller;

import be.willhunting.smecompliance.dto.InvoiceSubmissionRequest;
import be.willhunting.smecompliance.dto.InvoiceSubmissionResponse;
import be.willhunting.smecompliance.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public InvoiceSubmissionResponse submitInvoice(@Valid @RequestBody InvoiceSubmissionRequest request) {
        return invoiceService.submitInvoice(request);
    }
}
