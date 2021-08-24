package com.atmDispense.service;

import com.atmDispense.entity.ATMDenominations;
import com.atmDispense.error.ATMCashDispenserException;
import com.atmDispense.model.DispenseDenominationResult;

import java.util.List;

public interface ATMDenominationService {
   public List<ATMDenominations> getDenominations();
   public ATMDenominations findByDenomination(int denomination);
  // public String depositMoney(List<Denominations> denominations);
   public String depositMoney(DispenseDenominationResult denominations) throws ATMCashDispenserException;
   public DispenseDenominationResult withdrawMoney(int withdrawlAmount) throws ATMCashDispenserException;


}
