package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import com.zemoso.cashkick.cashkickApp.entities.User;
import com.zemoso.cashkick.cashkickApp.exception.DuplicateEntryException;
import com.zemoso.cashkick.cashkickApp.exception.ServiceException;
import com.zemoso.cashkick.cashkickApp.exception.UserNotFoundException;
import com.zemoso.cashkick.cashkickApp.mapper.CashKickMapper;
import com.zemoso.cashkick.cashkickApp.repository.CashKickRepository;
import com.zemoso.cashkick.cashkickApp.repository.UserRepository;
import com.zemoso.cashkick.cashkickApp.service.CashKickService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CashKickServiceImpl implements CashKickService {

    private final CashKickRepository cashKickRepository;

    private final UserRepository userRepository;

    public CashKickServiceImpl(CashKickRepository cashKickRepository, UserRepository userRepository) {
        this.cashKickRepository = cashKickRepository;
        this.userRepository = userRepository;
    }

    private static final ModelMapper modelMapper = new ModelMapper();


    @Override
    public List<CashKickDTO> getAllCashKicks(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        // Check if user is present in the Optional
        if (user.isPresent()) {
            List<CashKick> cashkickList=cashKickRepository.findAllDataById(userId);
            return cashkickList.stream()
                    .map(cashKick -> modelMapper.map(cashKick, CashKickDTO.class))
                    .toList();
        } else {
            throw new UserNotFoundException("User does not exist");
        }

    }

@Override
public CashKickDTO addCashKick(CashKickDTO cashKickDTO) throws ServiceException{
    // Check if a CashKick with the same name already exists
    Optional<CashKick> existingCashKick = cashKickRepository.findByCashkickName(cashKickDTO.getCashkickName());
    if (existingCashKick.isPresent()) {
        throw new DuplicateEntryException("A cashkick with name " + cashKickDTO.getCashkickName() + " already exists ");
    }

    // Convert the DTO to the CashKick entity
    CashKick cashKick = CashKickMapper.convertToEntity(cashKickDTO);

    // Validate the user if provided
    if (cashKickDTO.getUser() != null) {
        Optional<User> user = userRepository.findById(cashKickDTO.getUser().getId());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }
        cashKick.setUser(user.get());
    }

    // Save the CashKick entity and return the corresponding DTO
    CashKick createdCashkick = cashKickRepository.save(cashKick);
    return modelMapper.map(createdCashkick, CashKickDTO.class);
    }

}
