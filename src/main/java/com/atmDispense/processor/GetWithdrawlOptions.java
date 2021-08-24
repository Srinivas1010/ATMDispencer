package com.atmDispense.processor;

import com.atmDispense.entity.ATMDenominations;
import com.atmDispense.error.ATMCashDispenserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GetWithdrawlOptions {

    private  List<ATMDenominations> availableDenominations;
    private final Logger LOGGER =
            LoggerFactory.getLogger(GetWithdrawlOptions.class);

    public GetWithdrawlOptions(List<ATMDenominations> atmDenominations){
        this.availableDenominations=atmDenominations;
    }

    /*
    * This is an main method of processor which will list all the possible options to logs
    * and also evalutes best option to be choosen to dispense the cash.
    */
    public Integer[] getWithDrawlOptions(int withDrawlAmt) throws ATMCashDispenserException {
        List<Integer[]> results=getWithDrawlOptions(new int[availableDenominations.size()],withDrawlAmt,0);

        if(results.size()>0) {

            int k = 0;
            StringBuffer optionBuffer = new StringBuffer();
            optionBuffer.append("All possoble options to withdraw $" + withDrawlAmt + ":");
            optionBuffer.append("\n");
            for (Integer[] result : results) {
                optionBuffer.append("Option " + k);
                optionBuffer.append("\n");
                optionBuffer.append("-------------------------");
                optionBuffer.append("\n");
                int j = 0;
                StringBuffer sb = new StringBuffer();
                for (ATMDenominations atmDenominations : availableDenominations) {
                    sb.append("Denomination: " + atmDenominations.getDenomination());
                    sb.append(", Count: " + result[j]);
                    sb.append(", Total: " + atmDenominations.getDenomination() * result[j]);
                    sb.append("\n");
                    j++;
                }
                optionBuffer.append(sb.toString());
                optionBuffer.append("-------------------");
                optionBuffer.append("\n");
                k++;
            }
            System.out.println(optionBuffer.toString());
            // LOGGER.debug(optionBuffer.toString());
            int[] denomList = new int[availableDenominations.size()];
            int l = 0;
            for (ATMDenominations atmDenominations : availableDenominations) {
                denomList[l] = atmDenominations.getAvailableDenominationCount();
                l++;
            }
            System.out.println(Arrays.toString(denomList));

            if (results.size() > 1) {
                int n = 0;
                for (Integer[] result : results) {

                    List<Integer> list3 = IntStream.range(0, denomList.length).mapToObj(m -> denomList[m]
                            - result[m]).collect(Collectors.toList());
                    System.out.println("Availble Denomitions for each option" + list3);
                    if (!list3.contains(0))
                        break;
                    n++;
                }

                System.out.println("Best Option " + Arrays.toString(results.get(n)));
                return results.get(n);
            } else {
                return results.get(0);
            }
        }else{
            throw new ATMCashDispenserException("ATM cannot dispense the requested amount $"+withDrawlAmt+". Please change the amount.");
        }
    }

    /*
    * This method is an recursive overridden function to list all the possible options to withdraw
    * the amount based on the available denominations and denomination count.
    * For Ex : In case of $120 withdrawl
    *     Available Denominations {"50":"10",
    *                              "20":"10"}
    *           Result will be [{2,1},{0,6}]
    */
    public List<Integer[]> getWithDrawlOptions(int[] options, int withDrawlAmt, int position){

        List<Integer[]> list = new ArrayList<>();
        int value = compute(availableDenominations, options);
        if (value < withDrawlAmt){
            for (int i = position; i < availableDenominations.size(); i++) {
                if (availableDenominations.get(i).getAvailableDenominationCount() > options[i]){
                    int[] newvariation = options.clone();
                    newvariation[i]++;
                    List<Integer[]> newList = getWithDrawlOptions(newvariation, withDrawlAmt, i);
                    if (newList != null){
                        list.addAll(newList);
                    }
                }
            }
        } else if (value == withDrawlAmt) {
            list.add(myCopy(options));
        }

        return list;
    }

    /*
    *This method is used to compute the denomination value when multiplied by the no of units.
    */
    public static int compute(List<ATMDenominations> lstdenomination, int[] variation){
        int ret = 0;
        for (int i = 0; i < variation.length; i++) {
            ret += lstdenomination.get(i).getDenomination() * variation[i];
        }
        return ret;
    }

    /*
     *This method is used to clone arrays.
     */
    public static Integer[] myCopy(int[] ar){
        Integer[] ret = new Integer[ar.length];
        for (int i = 0; i < ar.length; i++) {
            ret[i] = ar[i];
        }
        return ret;
    }
}
