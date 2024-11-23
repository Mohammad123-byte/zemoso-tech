package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.dtos.ContractDTO;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import com.zemoso.cashkick.cashkickApp.entities.Contract;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.DuplicateEntryException;
import com.zemoso.cashkick.cashkickApp.exception.ServiceException;
import com.zemoso.cashkick.cashkickApp.exception.UserNotFoundException;
import com.zemoso.cashkick.cashkickApp.mapper.CashKickMapper;
import com.zemoso.cashkick.cashkickApp.repository.CashKickRepository;
import com.zemoso.cashkick.cashkickApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CashKickServiceImplTest {

    @Mock
    private CashKickRepository cashKickRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CashKickMapper cashKickMapper;  // Mock CashKickMapper

    @InjectMocks
    private CashKickServiceImpl cashKickService;

    private CashKickDTO cashKickDTO;
    private User user;

    private UserDTO  userDTO;

    @BeforeEach
    public void setUp() {
        // Initialize user object
        user = new User();
        user.setId(1);

        userDTO = new UserDTO();
        userDTO.setId(1);



        // Initialize CashKickDTO and assign user to it
        cashKickDTO = new CashKickDTO();
        cashKickDTO.setCashkickName("first");
        cashKickDTO.setUser(userDTO);  // Directly assign the user object
    }

    // Test for getAllCashKicks method
    @Test
    public void testGetAllCashKicks_Success() {
        // Arrange: Mock the necessary repository calls
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Prepare some mock data for CashKicks
        CashKick cashKick1 = new CashKick();
        cashKick1.setCashkickName("Cashkick 1");
        CashKick cashKick2 = new CashKick();
        cashKick2.setCashkickName("Cashkick 2");

        List<CashKick> cashKickList = new ArrayList<>();
        cashKickList.add(cashKick1);
        cashKickList.add(cashKick2);

        when(cashKickRepository.findAllDataById(1)).thenReturn(cashKickList);

        // Act: Call the method under test
        List<CashKickDTO> result = cashKickService.getAllCashKicks(1);

        // Assert: Verify that the result is as expected
        assertNotNull(result, "CashKicks list should not be null");
        assertEquals(2, result.size(), "The size of the returned list should be 2");
        assertEquals("Cashkick 1", result.get(0).getCashkickName(), "The first cashkick name should be 'Cashkick 1'");
        assertEquals("Cashkick 2", result.get(1).getCashkickName(), "The second cashkick name should be 'Cashkick 2'");
    }

    @Test
    public void testGetAllCashKicks_UserNotFound() {
        // Arrange: Mock the necessary repository calls
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Call the method and expect UserNotFoundException
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            cashKickService.getAllCashKicks(1);
        });

        assertEquals("User does not exist", exception.getMessage(), "Exception message should be 'User does not exist'");
    }
//
    @Test
    public void testGetAllCashKicks_NoCashKicksFound() {
        // Arrange: Mock the necessary repository calls
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(cashKickRepository.findAllDataById(1)).thenReturn(new ArrayList<>());

        // Act: Call the method under test
        List<CashKickDTO> result = cashKickService.getAllCashKicks(1);

        // Assert: Verify the result (should be empty list)
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.size(), "Result size should be 0 when no cashkicks found");
    }

    // Test for addCashKick method
    @Test
    public void testAddCashKick_Success() {
        // Arrange: Mock the necessary repository calls
        when(cashKickRepository.findByCashkickName("first")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        cashKickDTO = new CashKickDTO();

        // Initialize the DTO with necessary fields
        cashKickDTO.setCashkickName("first");
        cashKickDTO.setCashkickStatus("ACTIVE");
        cashKickDTO.setMaturityDate("2024-12-31");
        cashKickDTO.setTotalReceived(1000.0);
        cashKickDTO.setTermLength(12);
        cashKickDTO.setId(1);

        // Set UserDTO to cashKickDTO
        userDTO.setId(1);
        userDTO.setEmail("sample@gmail.com");
        userDTO.setUsername("sample");
        cashKickDTO.setUser(userDTO);
        cashKickDTO.setContracts(List.of(new ContractDTO())); // Assuming no contracts to begin with

        // Mock CashKickDTO to entity conversion
        Contract contract = new Contract();  // Assuming contract entity is simple
        List<Contract> contracts = List.of(contract);  // Add contract(s) to a list

        // Create CashKick entity and set the expected fields
        CashKick cashKick = new CashKick();
        cashKick.setCashkickName("first");
        cashKick.setCashkickStatus("ACTIVE");
        cashKick.setMaturityDate("2024-12-31");
        cashKick.setTotalReceived(1000.0);
        cashKick.setTermLength(12);
        cashKick.setUser(user);
        cashKick.setContracts(contracts);

        when(cashKickRepository.save(cashKick)).thenReturn(cashKick);


        // Mock the mapper method for converting DTO to entity
        try (MockedStatic<CashKickMapper> cashKickMapperMockedStatic = Mockito.mockStatic(CashKickMapper.class)) {
            cashKickMapperMockedStatic.when(() -> CashKickMapper.convertToEntity(cashKickDTO)).thenReturn(cashKick);

            // Act: Call the method under test
            CashKickDTO result = cashKickService.addCashKick(cashKickDTO);

            // Assert: Verify that the CashKickDTO is returned correctly
            assertNotNull(result, "The result should not be null");
            assertEquals("first", result.getCashkickName(), "The cashkick name should be 'first'");
            assertEquals("ACTIVE", result.getCashkickStatus(), "The cashkick status should be 'ACTIVE'");
            assertEquals(1000.0, result.getTotalReceived(), "The total received should be 1000.0");
            assertEquals(12, result.getTermLength(), "The term length should be 12");

            // Optionally, verify that the repository was called once
            verify(cashKickRepository, times(1)).save(any(CashKick.class));
        }
    }



    @Test
    public void testAddCashKick_DuplicateEntry() {
        // Arrange: Mock the necessary repository calls
        when(cashKickRepository.findByCashkickName("first")).thenReturn(Optional.of(new CashKick()));

        // Act & Assert: Call the method and expect DuplicateEntryException
        DuplicateEntryException exception = assertThrows(DuplicateEntryException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });

        assertEquals("A cashkick with name first already exists ", exception.getMessage(), "Exception message should match expected duplicate entry message");
    }

    @Test
    public void testAddCashKick_UserNotFound() {
        // Arrange: Mock the necessary repository calls
        when(cashKickRepository.findByCashkickName("first")).thenReturn(Optional.empty());
//        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Create a valid cashKickDTO object to pass to the service
        CashKickDTO cashKickDTO = new CashKickDTO();
        cashKickDTO.setId(1); // Assuming the DTO has a field for userId
        cashKickDTO.setCashkickName("first");

        // Act & Assert: Call the method and expect UserNotFoundException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });

    }


    @Test
    public void testAddCashKick_InternalError() {
        // Arrange: Mock the necessary repository calls
        when(cashKickRepository.findByCashkickName("first")).thenReturn(Optional.empty());
//        when(userRepository.findById(1)).thenThrow(new RuntimeException("Database error"));


        // Act & Assert: Call the method and expect ServiceException
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            cashKickService.addCashKick(cashKickDTO);
        });

        assertEquals("Fail to create cashkick due to internal error ", exception.getMessage(), "Exception message should match expected service error message");
    }

}
