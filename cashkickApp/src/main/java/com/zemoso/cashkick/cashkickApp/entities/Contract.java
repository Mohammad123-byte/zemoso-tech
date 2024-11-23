package com.zemoso.cashkick.cashkickApp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "contracts")  // Custom table name, optional if you want to use snake_case globally
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contract_name")
    private String contractName;

    @Column(name = "contract_type")
    private String contractType;

    @Column(name = "contract_status")
    private String contractStatus;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "payment_frequency")
    private String paymentFrequency;

    @Column(name = "contract_amount")
    private Double contractAmount;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "outstanding_amount")
    private Double outstandingAmount;

    @Column(name = "last_payment_date")
    private String lastPaymentDate;

    @Column(name = "start_amount")
    private Double startAmount;

    // Foreign key relationship with User
    @ManyToOne
    @JoinColumn(name = "userid") // Foreign key column in Contract table
    private User user;            // The user who owns this contract

    @ManyToMany(mappedBy = "contracts", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<CashKick> cashkick;

}