package com.atmDispense.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Denominations {
	private int id;
	private int denomination;
	private String denominationDescription;
	private int denominationCount;
	private int availableDenominationCount;
}
