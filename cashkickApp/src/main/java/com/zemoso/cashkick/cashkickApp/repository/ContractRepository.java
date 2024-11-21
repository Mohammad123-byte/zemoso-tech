package com.zemoso.cashkick.cashkickApp.repository;

import com.zemoso.cashkick.cashkickApp.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Integer> {

}
