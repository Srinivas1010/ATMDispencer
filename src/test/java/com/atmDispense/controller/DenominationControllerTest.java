package com.atmDispense.controller;

import com.atmDispense.entity.ATMDenominations;
import com.atmDispense.model.DispenseDenomination;
import com.atmDispense.model.DispenseDenominationResult;
import com.atmDispense.service.ATMDenominationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DenominationController.class)
class DenominationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ATMDenominationService atmDenominationService;

    private ATMDenominations atmDenominations;
    private DispenseDenominationResult dispenseDenominationResult;
    private DispenseDenomination dispenseDenomination;
   private List<ATMDenominations> denominationList=new ArrayList<ATMDenominations>();

   private String result;

    @BeforeEach
    void setUp() {
        atmDenominations = ATMDenominations.builder()
                .denomination(50)
                .denominationCount(10)
                .availableDenominationCount(10)
                .denominationDescription("$50 Denomination")
                .denominationId(1)
                .build();

        denominationList.add(atmDenominations);


        dispenseDenomination=DispenseDenomination.builder()
                .denomination(50)
                .denominationCount(2)
                .build();
        List<DispenseDenomination> dispenseDenominationList=new ArrayList<DispenseDenomination>();
        dispenseDenominationResult =DispenseDenominationResult.builder()
                .dispenseDenomination(dispenseDenominationList)
                .totalBalance(100).build();



    }



    @Test
    void getDenominations() throws Exception {
        Mockito.when(atmDenominationService.getDenominations())
                .thenReturn(denominationList);

        mockMvc.perform(get("/denominations/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    /*@Test
    void testDeposit() throws Exception {
        Mockito.when(atmDenominationService.depositMoney(dispenseDenominationResult))
                .thenReturn(result);

        mockMvc.perform(get("/denominations/deposit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(atmDenominationService.findByDenomination(50))
                .thenReturn(atmDenominations);

        mockMvc.perform(get("/denominations/50")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableDenominationCount").
                        value(12));
    }*/
}