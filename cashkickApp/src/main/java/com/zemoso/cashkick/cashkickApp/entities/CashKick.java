package com.zemoso.cashkick.cashkickApp.entities;

import com.fasterxml.jackson.annotation.*;
import com.zemoso.cashkick.cashkickApp.dtos.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
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


    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CashKick{")
                .append("id=").append(id)
                .append(", cashkickName='").append(cashkickName).append('\'')
                .append(", cashkickStatus='").append(cashkickStatus).append('\'')
                .append(", maturityDate='").append(maturityDate).append('\'')
                .append(", totalReceived=").append(totalReceived)
                .append(", termLength=").append(termLength);

        // Optionally, add user information (user ID)
        sb.append(", user_id=").append(user != null ? user.getUserid() : "null");

        // Optionally, add the first few contract IDs (avoid full contract serialization)
        if (contracts != null && !contracts.isEmpty()) {
            sb.append(", first_contracts=[");
            for (int i = 0; i < Math.min(contracts.size(), 3); i++) {
                sb.append(contracts.get(i).getId());  // Assuming Contract has `getId()` method
                if (i < Math.min(contracts.size(), 3) - 1) sb.append(", ");
            }
            sb.append("]");
        }

        sb.append('}');
        return sb.toString();
    }
*/
}
