package com.atmDispense.repository;


import com.atmDispense.entity.ATMDenominations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenominationRepository extends JpaRepository<ATMDenominations, Integer> {

	public ATMDenominations findByDenomination(int denomination);
	
}
