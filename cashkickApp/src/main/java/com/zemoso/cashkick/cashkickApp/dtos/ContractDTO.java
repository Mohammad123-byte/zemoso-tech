package com.zemoso.cashkick.cashkickApp.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ContractDTO {

    private Integer id;

    // The contract name must not be blank
    @NotBlank(message = "Contract Name cannot be empty")
    private String contractName;

    // The contract type can be optional, but if provided, must not be blank
    @NotBlank(message = "Contract Type cannot be empty", groups = {Update.class})  // Optional in creation, required in updates
    private String contractType;

    // Optional but needs to be a valid contract status if provided
    @Pattern(regexp = "^(Active|Inactive|Completed)$", message = "Contract Status must be one of 'Active', 'Inactive', or 'Completed'")
    private String contractStatus;

    // Start and end dates should follow a specific format (yyyy-MM-dd)
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Start Date must be in format dd-MM-yyyy")
    private String startDate;

    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "End Date must be in format dd-MM-yyyy")
    private String endDate;

    // Optional: Payment frequency can be a string
    private String paymentFrequency;

    // The contract amount should be a positive number
    @NotNull(message = "Contract Amount is required")
    @Positive(message = "Contract Amount must be a positive number")
    private Double contractAmount;

    // The paid amount should not be negative
    @NotNull(message = "Paid Amount is required")
    @PositiveOrZero(message = "Paid Amount must be a non-negative number")
    private Double paidAmount;

    // The outstanding amount should be non-negative
    @NotNull(message = "Outstanding Amount is required")
    @PositiveOrZero(message = "Outstanding Amount must be a non-negative number")
    private Double outstandingAmount;

    // Optional: Last payment date can be provided if available
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Last Payment Date must be in format yyyy-MM-dd")
    private String lastPaymentDate;

    // The start amount should be a positive number
    @NotNull(message = "Start Amount is required")
    @Positive(message = "Start Amount must be a positive number")
    private Double startAmount;

    // User ID (should not be null if creating a contract)
    @NotNull(message = "User ID is required")
    private Integer userId;

    // CashKick ID (should not be null if creating a contract)
    @NotNull(message = "CashKick ID is required")
    private Integer cashkickId;

    // Group validation for update scenario (e.g., contractType is optional for creation but required for updates)
    public interface Update {}
}
