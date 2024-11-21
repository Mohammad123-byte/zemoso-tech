package com.zemoso.cashkick.cashkickApp.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer userid;

//    private Integer user_id;
    @JsonIgnore
    @Column(name = "user_name")
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;

    // One user can have many cashkicks
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CashKick> cashkicks;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{")
                .append("userid=").append(userid)
                .append(", username='").append(username).append('\'')
                .append(", email='").append(email).append('\'')
                .append(", cashkicks_count=").append(cashkicks != null ? cashkicks.size() : 0);

        // Optionally, add first few cashkick ids (to prevent recursion)
        if (cashkicks != null && !cashkicks.isEmpty()) {
            sb.append(", first_cashkicks=[");
            for (int i = 0; i < Math.min(cashkicks.size(), 3); i++) {
                sb.append(cashkicks.get(i).getId());  // assuming CashKick has a `getId()` method
                if (i < Math.min(cashkicks.size(), 3) - 1) sb.append(", ");
            }
            sb.append("]");
        }

        sb.append('}');
        return sb.toString();
    }

}



























































//    // One user can have many contracts
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    @JsonIgnore
//    private List<Contract> contracts;