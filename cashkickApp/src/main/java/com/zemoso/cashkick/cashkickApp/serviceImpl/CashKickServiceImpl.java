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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CashKickServiceImpl implements CashKickService {

    private final CashKickRepository cashKickRepository;

    public CashKickServiceImpl(CashKickRepository cashKickRepository){
        this.cashKickRepository = cashKickRepository;
    }

    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CashKickDTO> getAllCashKicks(Integer userId) {
//        List<CashKick> cashkickList=cashKickRepository.findAllDataByUserid(userId);

        Optional<User> user = userRepository.findById(userId);
        // Check if user is present in the Optional

        if (user.isPresent()) {
            List<CashKick> cashkickList=cashKickRepository.findAllDataById(userId);
            //convert cashkick to cashkickDTO
            return cashkickList.stream()
                    .map(cashKick -> modelMapper.map(cashKick, CashKickDTO.class))
                    .toList();
        } else {
            throw new UserNotFoundException("User does not exist");
        }

    }

    @Override
    public CashKickDTO addCashKick(CashKickDTO cashKickDTO) {
        try {
//            Integer uId=cashKickDTO.getUser().getUserid();
            Integer uId=cashKickDTO.getUser().getId();
            Optional<CashKick> existingCashKick = cashKickRepository.findByCashkickName(cashKickDTO.getCashkickName());
            if (existingCashKick.isPresent()) {
                throw new DuplicateEntryException("A cashkick with name " + cashKickDTO.getCashkickName() + " already exists ");
            }

            CashKick cashKick = CashKickMapper.convertToEntity(cashKickDTO);

            /*if (cashKickDTO.getUser()!= null) {
                User user=userRepository.findByUserid(uId);
                cashKick.setUser(user);
                if (user == null) {
                    throw new UserNotFoundException("User does not exists ");
                }
            }*/

            if (cashKickDTO.getUser() != null) {
                // Use findById() to get an Optional<User> object
                Optional<User> user = userRepository.findById(uId);

                // Check if user is present in the Optional
                if (user.isPresent()) {
                    cashKick.setUser(user.get());
                } else {
                    throw new UserNotFoundException("User does not exist");
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
