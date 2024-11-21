package com.zemoso.cashkick.cashkickApp.repository;

import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashKickRepository extends JpaRepository<CashKick,Integer> {

    Optional<CashKick> findByCashkickName(@NotBlank(message = "cash kick name is mandatory") String cashkickName);

    @Query(value = "SELECT * FROM cashkicks WHERE id = :id", nativeQuery = true)
    List<CashKick> findAllDataById(@Param("id") Integer id);



}
