package com.atmDispense.controller;

import com.atmDispense.entity.ATMDenominations;
import com.atmDispense.error.ATMCashDispenserException;
import com.atmDispense.model.DispenseDenominationResult;
import com.atmDispense.model.TransactionResponse;
import com.atmDispense.service.ATMDenominationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DenominationController {

    @Autowired
    private ATMDenominationService atmDenominationService;

    private final Logger LOGGER =
            LoggerFactory.getLogger(DenominationController.class);

    @GetMapping("/denominations")
    public List<ATMDenominations> getDenominations(){
        return atmDenominationService.getDenominations();
    }

    @GetMapping("/denominations/{denomination}")
    public ATMDenominations findByDenomination(@PathVariable("denomination") int denomination){
        return atmDenominationService.findByDenomination(denomination);
    }

    @PostMapping("/denominations/deposit")
    public ResponseEntity depositMoney(@RequestBody DispenseDenominationResult depositAmount) throws ATMCashDispenserException {
       String txnStatus= atmDenominationService.depositMoney(depositAmount);
        ResponseEntity respEntity;
            respEntity= populateResponseEntity("Amount deposited successfully");
        return respEntity;
    }

    @PostMapping("/denominations/withdraw/{withdrawAmt}")
    public DispenseDenominationResult withdrawMoney(@PathVariable("withdrawAmt") int withdrawAmt) throws ATMCashDispenserException {
        DispenseDenominationResult dispenseDenominationResult=atmDenominationService.withdrawMoney(withdrawAmt);
        return dispenseDenominationResult;
    }

    private ResponseEntity populateResponseEntity(String msg) {
        TransactionResponse trnResponseStat =
                new TransactionResponse();
        trnResponseStat.setStatusCode(0);
        trnResponseStat.setTransactionStatus(msg);

        return new ResponseEntity<TransactionResponse>(trnResponseStat,
                HttpStatus.OK);
    }


}
