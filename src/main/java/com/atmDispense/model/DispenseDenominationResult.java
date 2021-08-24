package com.atmDispense.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispenseDenominationResult {

	private int totalBalance;
	private List<DispenseDenomination> dispenseDenomination;
}
