package com.zemoso.cashkick.cashkickApp.mapper;

import com.zemoso.cashkick.cashkickApp.dtos.ContractDTO;
import com.zemoso.cashkick.cashkickApp.entities.Contract;
import org.modelmapper.ModelMapper;

public class ContractMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        // Custom configurations if needed
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    // Convert entity to DTO
    public static ContractDTO convertToDTO(Contract contract) {
        if (contract == null) {
            return null;
        }
        return modelMapper.map(contract, ContractDTO.class);
    }

    // Convert DTO to entity
    public static Contract convertToEntity(ContractDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }
        return modelMapper.map(contractDTO, Contract.class);
    }
}
