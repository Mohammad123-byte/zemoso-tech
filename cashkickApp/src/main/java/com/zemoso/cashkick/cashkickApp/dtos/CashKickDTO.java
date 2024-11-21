package com.zemoso.cashkick.cashkickApp.dtos;

import com.zemoso.cashkick.cashkickApp.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CashKickDTO {

    private Integer id;
    @NotBlank(message = "cash kick name is mandatory")
    private String cashkickName;
    private String cashkickStatus;
    private String maturityDate;
    private Double totalReceived;
    @NotNull(message = "termLength can not be null")
    @Min(value = 1,message = "termLength must be a positive number")
    private Integer termLength;

    @NotNull(message = "user cannot be null")
    private UserDTO user;

    @NotNull(message = "contracts cannot be null")
    @Valid
    private List<ContractDTO> contracts;

}

