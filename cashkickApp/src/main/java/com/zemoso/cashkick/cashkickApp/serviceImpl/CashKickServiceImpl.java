package com.zemoso.cashkick.cashkickApp.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoso.cashkick.cashkickApp.controllers.UserController;
import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.DataInput;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CashKickServiceImpl implements CashKickService {

    @Autowired
    private CashKickRepository cashKickRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CashKickServiceImpl.class);


    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CashKickDTO> getAllCashKicks(Integer userId) {
        List<CashKick> cashkickList=cashKickRepository.findAllDataByUserid(userId);
        //convert cashkick to cashkickDTO
        return cashkickList.stream()
                .map(cashKick -> modelMapper.map(cashKick, CashKickDTO.class))
                .toList();
    }

    @Override
    public CashKickDTO addCashKick(CashKickDTO cashKickDTO) {
        try {
            Integer uId=cashKickDTO.getUser().getUserid();
            Optional<CashKick> existingCashKick = cashKickRepository.findByCashkickName(cashKickDTO.getCashkickName());
            if (existingCashKick.isPresent()) {
                throw new DuplicateEntryException("A cashkick with name " + cashKickDTO.getCashkickName() + " already exists ");
            }

            CashKick cashKick = CashKickMapper.convertToEntity(cashKickDTO);

            if (cashKickDTO.getUser()!= null) {
                User user=userRepository.findByUserid(uId);
                cashKick.setUser(user);
                if (user == null) {
                    throw new UserNotFoundException("User does not exists ");
                }
            }

            CashKick createdCashkick = cashKickRepository.save(cashKick);
            return modelMapper.map(createdCashkick,CashKickDTO.class);

        }catch (DuplicateEntryException | UserNotFoundException e) {
            throw e; // Re-throw Exception so it propagates
        }catch (Exception e) {
            throw new ServiceException("Fail to create cashkick due to internal error ");
        }

    }
}
