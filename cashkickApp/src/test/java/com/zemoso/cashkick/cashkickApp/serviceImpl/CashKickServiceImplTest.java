package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.dtos.ContractDTO;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.DuplicateEntryException;
import com.zemoso.cashkick.cashkickApp.exception.ServiceException;
import com.zemoso.cashkick.cashkickApp.repository.CashKickRepository;
import com.zemoso.cashkick.cashkickApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CashKickServiceImplTest {

    @Mock
    private CashKickRepository cashKickRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CashKickServiceImpl cashKickService;

    private CashKickDTO cashKickDTO;
    private User user;
    private ContractDTO contract;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new User();
        user.setUserid(1);

        cashKickDTO = new CashKickDTO();
        cashKickDTO.setCashkickName("first");
        cashKickDTO.setUser(new UserDTO());
        cashKickDTO.getUser().setUserid(1);
    }

    @Test
    public void testAddCashKick_Success() {
        // Arrange
        when(cashKickRepository.findByCashkickName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUserid(anyInt())).thenReturn(user);

        CashKick cashKick = new CashKick();
        cashKick.setCashkickName("first");

        when(cashKickRepository.save(any(CashKick.class))).thenReturn(cashKick);


        user = new User();
        user.setUserid(1);
        contract = new ContractDTO();
        contract.setContractName("contract1");

        cashKickDTO = new CashKickDTO();
        cashKickDTO.setCashkickName("first");
        cashKickDTO.setUser(new UserDTO());
        cashKickDTO.getUser().setUserid(1);
        ArrayList<ContractDTO> contractList=new ArrayList<>();
        contractList.add(contract);
        cashKickDTO.setContracts(contractList);


        // Act
        CashKickDTO result = cashKickService.addCashKick(cashKickDTO);

        // Assert
        assertNotNull(result);
        assertEquals("first", result.getCashkickName());
        verify(cashKickRepository).save(any(CashKick.class));
    }

    @Test
    public void testAddCashKick_DuplicateEntry() {
        // Arrange
        when(cashKickRepository.findByCashkickName(anyString())).thenReturn(Optional.of(new CashKick()));

        // Act & Assert
        DuplicateEntryException exception = assertThrows(DuplicateEntryException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });
        assertEquals("A cashkick with name first already exists ", exception.getMessage());
    }

    @Test
    public void testAddCashKick_UserNotFound() {
        // Arrange
        when(cashKickRepository.findByCashkickName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUserid(anyInt())).thenReturn(null);  // Simulate user not found

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });

        assertEquals("Fail to create cashkick due to internal error ", exception.getMessage());
    }


    @Test
    public void testAddCashKick_InternalError() {
        // Arrange
        when(cashKickRepository.findByCashkickName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUserid(anyInt())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });
        assertEquals("Fail to create cashkick due to internal error ", exception.getMessage());
    }

    @Test
    public void testGetAllCashKicks_Success() {
        // Arrange
        Integer userId = 1;

        // Create mock CashKick entities
        CashKick cashKick1 = new CashKick();
        cashKick1.setCashkickName("Cashkick 1");
        cashKick1.setUser(user);

        CashKick cashKick2 = new CashKick();
        cashKick2.setCashkickName("Cashkick 2");
        cashKick2.setUser(user);

        // Mock the repository to return the list of CashKick entities
        when(cashKickRepository.findAllDataByUserid(userId)).thenReturn(List.of(cashKick1, cashKick2));

        // Act
        List<CashKickDTO> result = cashKickService.getAllCashKicks(userId);

        // Assert
        assertNotNull(result); // Ensure the result is not null
        assertEquals(2, result.size()); // Check that the list contains two elements

        // Verify that CashKickDTO mapping is correct
        assertEquals("Cashkick 1", result.get(0).getCashkickName());
        assertEquals("Cashkick 2", result.get(1).getCashkickName());

        // Verify that the repository method was called
        verify(cashKickRepository).findAllDataByUserid(userId);
    }

}
