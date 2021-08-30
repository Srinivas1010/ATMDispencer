package com.atmDispense.service;

import com.atmDispense.entity.ATMDenominations;
import com.atmDispense.error.ATMCashDispenserException;
import com.atmDispense.model.DispenseDenomination;
import com.atmDispense.model.DispenseDenominationResult;
import com.atmDispense.processor.GetWithdrawlOptions;
import com.atmDispense.repository.DenominationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ATMDenominationServiceImpl implements ATMDenominationService {

    @Autowired
    private DenominationRepository denominationRepository;

    @Value("${atm.denominations.allowed}")
    private String allowedDenominations;

    @Override
    public List<ATMDenominations> getDenominations() {
        List<ATMDenominations>  denominationList=denominationRepository.findAll(Sort.by(Sort.Direction.DESC, "denomination"));
        return denominationList;
    }

    @Override
    public ATMDenominations findByDenomination(int denomination) {
        ATMDenominations  denominationObj=denominationRepository.
                findByDenomination(denomination);
        return denominationObj;
    }

  /*  @Override
    @Transactional
    public String depositMoney(List<Denominations> depositAmount) {
        String result="SUCCESS";
       // TransactionResponse trnResponseStat = new TransactionResponse();
        try {
            for (Denominations denomination : depositAmount) {
                ATMDenominations dbDenominations = denominationRepository.findByDenomination(denomination.getDenomination());
                if (dbDenominations == null) {
                    dbDenominations = new ATMDenominations();
                    dbDenominations.setAvailableDenominationCount(denomination.getDenominationCount());
                    dbDenominations.setDenominationCount(denomination.getDenominationCount());
                    dbDenominations.setDenomination(denomination.getDenomination());
                    dbDenominations.setDenominationDescription(denomination.getDenominationDescription());
                    denominationRepository.save(dbDenominations);
                } else {
                    dbDenominations.setAvailableDenominationCount(dbDenominations.getAvailableDenominationCount() +
                            denomination.getDenominationCount());
                    dbDenominations.setDenominationCount(dbDenominations.getDenominationCount() +
                            denomination.getDenominationCount());
                    //  denominationRepository.save(dbDenominations);
                }
            }
        }catch (Exception e){
            result="ERROR";
        }

        return result;
    }*/

    @Override
    @Transactional
    public String depositMoney(DispenseDenominationResult depositAmount) throws ATMCashDispenserException {
        String result="SUCCESS";
       // TransactionResponse trnResponseStat = new TransactionResponse();
        try {

            //TODO validate total balance and allowed denominations
            int calculatedBalance=0;
            for (DispenseDenomination denomination : depositAmount.getDispenseDenomination()) {
                boolean denomContains= Arrays.stream(allowedDenominations.split(",")).anyMatch(String.valueOf(denomination.getDenomination())::equals);
                if(!denomContains){
                    throw new ATMCashDispenserException("Denomination $"+denomination.getDenomination()+" is not supported");
                }
                calculatedBalance+=denomination.getDenominationCount()*denomination.getDenomination();
            }

            if(depositAmount.getTotalBalance() != calculatedBalance){
               throw new ATMCashDispenserException("Total balance dosen't match with the provided denominations");
            }


            for (DispenseDenomination denomination : depositAmount.getDispenseDenomination()) {
                ATMDenominations dbDenominations = denominationRepository.findByDenomination(denomination.getDenomination());
                if (dbDenominations == null) {
                    dbDenominations = new ATMDenominations();
                    dbDenominations.setAvailableDenominationCount(denomination.getDenominationCount());
                    dbDenominations.setDenominationCount(denomination.getDenominationCount());
                    dbDenominations.setDenomination(denomination.getDenomination());
                    //dbDenominations.setDenominationDescription(denomination.getDenominationDescription());
                    denominationRepository.save(dbDenominations);
                } else {
                    dbDenominations.setAvailableDenominationCount(dbDenominations.getAvailableDenominationCount() +
                            denomination.getDenominationCount());
                    dbDenominations.setDenominationCount(dbDenominations.getDenominationCount() +
                            denomination.getDenominationCount());
                    //  denominationRepository.save(dbDenominations);
                }
            }
        }catch (Exception e){
            throw new ATMCashDispenserException(e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional
    public DispenseDenominationResult withdrawMoney(int withdrawlAmount) throws ATMCashDispenserException {
        DispenseDenominationResult dispenseDenominationResult=new DispenseDenominationResult();
        List<DispenseDenomination> lstDispenseDenomination=new ArrayList<DispenseDenomination>();
        dispenseDenominationResult.setDispenseDenomination(lstDispenseDenomination);
        dispenseDenominationResult.setTotalBalance(withdrawlAmount);

        try {
            List<ATMDenominations> denominationList = denominationRepository.findAll(Sort.by(Sort.Direction.DESC, "denomination"));
            List<ATMDenominations> denominationListfiltered = denominationList.stream().filter(denom -> denom.getAvailableDenominationCount()>0).collect(Collectors.toList());
            if(denominationListfiltered.size()==0) {
                throw new ATMCashDispenserException("Denominations dosen't exist to dispense $" + withdrawlAmount);
                //TODO Should trigger Notification to inform about no-cash
            }

            GetWithdrawlOptions withdrawloptions = new GetWithdrawlOptions(denominationListfiltered);
            Integer[] possibleOption = withdrawloptions.getWithDrawlOptions(withdrawlAmount);

            int j = 0;
            for (ATMDenominations atmDenominations : denominationListfiltered) {
                DispenseDenomination dispenseDenomination = new DispenseDenomination();
                dispenseDenomination.setDenomination(atmDenominations.getDenomination());
                dispenseDenomination.setDenominationCount(possibleOption[j]);
                dispenseDenomination.setTotal(atmDenominations.getDenomination() * possibleOption[j]);
                dispenseDenominationResult.getDispenseDenomination().add(dispenseDenomination);

                atmDenominations.setAvailableDenominationCount(atmDenominations.getAvailableDenominationCount() - possibleOption[j]);

                if(atmDenominations.getAvailableDenominationCount()==0){
                    //TODO Should trigger Notification to inform about no-cash
		}

                j++;
            }
        }catch (Exception e){
            throw new ATMCashDispenserException(e.getMessage());
        }

        return dispenseDenominationResult;
    }
}
