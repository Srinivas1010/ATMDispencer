package com.atmDispense.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name="seq", initialValue=3, allocationSize=100)
public class ATMDenominations {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq")
	private int denominationId;
	private int denomination;
	private String denominationDescription;
	private int denominationCount;
	private int availableDenominationCount;

}
