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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new User();
        user.setId(1);

        cashKickDTO = new CashKickDTO();
        cashKickDTO.setCashkickName("first");
        cashKickDTO.setUser(new UserDTO());
        cashKickDTO.getUser().setId(1);
    }

   /* @Test
    public void testAddCashKick_Success() {
        // Arrange
        // Mock the cashKickRepository to return empty for findByCashkickName
        when(cashKickRepository.findByCashkickName(anyString())).thenReturn(Optional.empty());

        // Mock the userRepository to return a valid user when findById is called
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        // Create and mock a CashKick entity to be saved
        CashKick cashKick = new CashKick();
        cashKick.setCashkickName("first");
        cashKick.setUser(user);

        // Mock the cashKickRepository.save to return the CashKick we just created
        when(cashKickRepository.save(any(CashKick.class))).thenReturn(cashKick);

        // Mocking the setup of contracts in CashKickDTO
        ContractDTO contract = new ContractDTO();
        contract.setContractName("contract1");

        cashKickDTO.setCashkickName("first");
        cashKickDTO.setUser(new UserDTO());
        cashKickDTO.getUser().setId(1);

        List<ContractDTO> contractList = new ArrayList<>();
        contractList.add(contract);
        cashKickDTO.setContracts(contractList);

        // Act
        CashKickDTO result = cashKickService.addCashKick(cashKickDTO);

        // Assert
        assertNotNull(result);  // Ensure that the result is not null
        assertEquals("first", result.getCashkickName());  // Check the CashKick name
        assertEquals(1, result.getUser().getId());  // Ensure the user is correctly assigned

        // Verify the save method was called on the cashKickRepository
        verify(cashKickRepository, times(1)).save(any(CashKick.class));

        // Verify that no exceptions are thrown
        assertDoesNotThrow(() -> {
            cashKickService.addCashKick(cashKickDTO);
        });
    }*/



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
        when(userRepository.findById(anyInt())).thenReturn(null);  // Simulate user not found

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
        when(userRepository.findById(anyInt())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });
        assertEquals("Fail to create cashkick due to internal error ", exception.getMessage());
    }

   /* @Test
    public void testGetAllCashKicks_Success() {
        // Arrange
        Integer id = 1;

        // Create mock CashKick entities
        CashKick cashKick1 = new CashKick();
        cashKick1.setCashkickName("Cashkick 1");
        cashKick1.setUser(user);

        CashKick cashKick2 = new CashKick();
        cashKick2.setCashkickName("Cashkick 2");
        cashKick2.setUser(user);

        // Mock the repository to return the list of CashKick entities
        when(cashKickRepository.findAllDataById(id)).thenReturn(List.of(cashKick1, cashKick2));

        // Act
        List<CashKickDTO> result = cashKickService.getAllCashKicks(id);

        // Assert
        assertNotNull(result); // Ensure the result is not null
        assertEquals(2, result.size()); // Check that the list contains two elements

        // Verify that CashKickDTO mapping is correct (assuming service converts to DTO)
        assertEquals("Cashkick 1", result.get(0).getCashkickName());
        assertEquals("Cashkick 2", result.get(1).getCashkickName());

        // Verify that the repository method was called once
        verify(cashKickRepository, times(1)).findAllDataById(id);
    }
*/
}
