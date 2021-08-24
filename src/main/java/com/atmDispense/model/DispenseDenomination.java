package com.atmDispense.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispenseDenomination {

	private int denomination;
	private int denominationCount;
	private int total;
}
