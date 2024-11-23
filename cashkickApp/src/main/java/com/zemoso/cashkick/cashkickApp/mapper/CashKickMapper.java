package com.zemoso.cashkick.cashkickApp.mapper;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.dtos.ContractDTO;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import com.zemoso.cashkick.cashkickApp.entities.Contract;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
public class CashKickMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    // Convert entity to DTO
    public static CashKickDTO convertToDTO(CashKick cashKick) {
        if (cashKick == null) {
            return null;
        }

        CashKickDTO dto = modelMapper.map(cashKick, CashKickDTO.class);

        // Convert contracts using modelMapper
        List<ContractDTO> contractDTOList = modelMapper.map(cashKick.getContracts(), new TypeToken<List<ContractDTO>>() {}.getType());
        dto.setContracts(contractDTOList);

        // Convert User to UserDTO
        if (cashKick.getUser() != null) {
            UserDTO userDTO = modelMapper.map(cashKick.getUser(), UserDTO.class);
            dto.setUser(userDTO);
        }

        return dto;
    }

    // Convert DTO to entity
    public static CashKick convertToEntity(CashKickDTO cashKickDTO) {
        if (cashKickDTO == null) {
            return null;
        }

        CashKick cashKick = modelMapper.map(cashKickDTO, CashKick.class);

        // Convert contracts using modelMapper
        List<Contract> contractList = modelMapper.map(cashKickDTO.getContracts(), new TypeToken<List<Contract>>() {}.getType());
        cashKick.setContracts(contractList);


        return cashKick;
    }
}
