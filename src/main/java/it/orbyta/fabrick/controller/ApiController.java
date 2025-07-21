package it.orbyta.fabrick.controller;

import it.orbyta.fabrick.dto.request.FabricMoneyTransferRequest;
import it.orbyta.fabrick.dto.response.FabricApiBaseResponse;
import it.orbyta.fabrick.service.FabrickApiService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<FabricApiBaseResponse> getTransaction() throws Exception {
        return ResponseEntity.ok(fabrickApiService.getTransactionList(LocalDate.of(2024, 12, 1), LocalDate.now()));
    }


    @PostMapping("/transfer")
    public ResponseEntity<FabricApiBaseResponse> postTransferMoney(@RequestBody FabricMoneyTransferRequest request) throws Exception {
        return ResponseEntity.ok(fabrickApiService.moneyTransfer(request));
    }
}
