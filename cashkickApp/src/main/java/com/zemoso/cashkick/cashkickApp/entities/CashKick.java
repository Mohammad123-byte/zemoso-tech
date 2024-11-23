package com.zemoso.cashkick.cashkickApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "cashkicks")
public class CashKick  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cashkick_name")
    private String cashkickName;

    @Column(name = "cashkick_status")
    private String cashkickStatus;

    @Column(name = "maturity_date")
    private String maturityDate;

    @Column(name = "total_received")
    private Double totalReceived;

    @Column(name = "term_length")
    private Integer termLength;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private User user;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "Cashkick_Contract_Table",
            joinColumns = {
                    @JoinColumn(name = "cashkick_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "contract_id", referencedColumnName = "id")
            }
    )
    @JsonManagedReference // Prevents infinite recursion from CashKick -> Contract -> CashKick
    @NotNull(message = "contracts can not be null ")
    private List<Contract> contracts;


}