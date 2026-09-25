package be.willhunting.smecompliance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.willhunting.smecompliance.dto.InvoiceSubmissionRequest;
import be.willhunting.smecompliance.dto.InvoiceSubmissionResponse;
import be.willhunting.smecompliance.exception.InvalidVatNumberException;
import be.willhunting.smecompliance.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InvoiceService invoiceService;

    @Test
    void submitInvoiceReturnsAcceptedInvoiceWhenVatNumberIsValid() throws Exception {
        when(invoiceService.submitInvoice(any(InvoiceSubmissionRequest.class)))
                .thenReturn(new InvoiceSubmissionResponse("INV-2026-0001", "BE0123456749", true, "ACCEPTED"));

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "invoiceNumber": "INV-2026-0001",
                                  "vatNumber": "BE0123456749",
                                  "issueDate": "2026-09-23",
                                  "totalAmount": 125.50
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-2026-0001"))
                .andExpect(jsonPath("$.vatNumber").value("BE0123456749"))
                .andExpect(jsonPath("$.vatValid").value(true))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    void submitInvoiceReturnsUnprocessableEntityWhenVatNumberIsInvalid() throws Exception {
        when(invoiceService.submitInvoice(any(InvoiceSubmissionRequest.class)))
                .thenThrow(new InvalidVatNumberException("BE0123456789"));

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "invoiceNumber": "INV-2026-0001",
                                  "vatNumber": "BE0123456789",
                                  "issueDate": "2026-09-23",
                                  "totalAmount": 125.50
                                }
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_VAT_NUMBER"));
    }

    @Test
    void submitInvoiceReturnsBadRequestWhenRequestValidationFails() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "invoiceNumber": "",
                                  "vatNumber": "BE0123456749",
                                  "issueDate": "2026-09-23",
                                  "totalAmount": 125.50
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }
}
