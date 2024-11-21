package com.zemoso.cashkick.cashkickApp.controllers;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.service.CashKickService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CashKickControllerTest {

    @Mock
    private CashKickService cashKickService;

    @InjectMocks
    private CashKickController cashKickController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCashkick() {
        // Arrange
        CashKickDTO cashKickDTO = new CashKickDTO();
        cashKickDTO.setCashkickName("Test Cashkick");
        cashKickDTO.setCashkickStatus("Active");
        cashKickDTO.setMaturityDate("2025-12-31");
        cashKickDTO.setTotalReceived(1000.0);
        cashKickDTO.setTermLength(12);

        CashKickDTO createdCashKickDTO = new CashKickDTO();
        createdCashKickDTO.setId(1);
        createdCashKickDTO.setCashkickName("Test Cashkick");

        when(cashKickService.addCashKick(any(CashKickDTO.class))).thenReturn(createdCashKickDTO);

        // Act
        ResponseEntity<CashKickDTO> response = cashKickController.createCashkick(cashKickDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Cashkick", response.getBody().getCashkickName());
    }

    @Test
    void testGetAllCashkicks() {
        // Arrange
        Integer userId = 1;
        CashKickDTO cashKickDTO1 = new CashKickDTO();
        cashKickDTO1.setCashkickName("Cashkick 1");
        CashKickDTO cashKickDTO2 = new CashKickDTO();
        cashKickDTO2.setCashkickName("Cashkick 2");

        List<CashKickDTO> cashKickDTOList = Arrays.asList(cashKickDTO1, cashKickDTO2);
        when(cashKickService.getAllCashKicks(userId)).thenReturn(cashKickDTOList);

        // Act
        ResponseEntity<List<CashKickDTO>> response = cashKickController.getAllCashkicks(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Cashkick 1", response.getBody().get(0).getCashkickName());
    }
}
