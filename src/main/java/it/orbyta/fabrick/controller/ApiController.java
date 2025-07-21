package it.orbyta.fabrick.controller;

import it.orbyta.fabrick.dto.request.FabricMoneyTransferRequest;
import it.orbyta.fabrick.dto.response.FabricApiBaseResponse;
import it.orbyta.fabrick.service.FabrickApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    FabrickApiService fabrickApiService;

    @GetMapping("/balance")
    public ResponseEntity<FabricApiBaseResponse> getBalance() throws Exception {
        return ResponseEntity.ok(fabrickApiService.getBalance());
    }

    @GetMapping("/transaction")
    public ResponseEntity<FabricApiBaseResponse> getTransaction(@RequestParam(name = "from")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                @RequestParam(name = "to")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(fabrickApiService.getTransactionList(from, to));
    }

    @PostMapping("/transfer")
    public ResponseEntity<FabricApiBaseResponse> postTransferMoney(@Valid @RequestBody FabricMoneyTransferRequest request) throws Exception {
        return ResponseEntity.ok(fabrickApiService.moneyTransfer(request));
    }
}
