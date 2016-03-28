package com.library;

public class Receipt {

	private Double extraDayCharges;
	
	public Receipt(Double extraDayCharges) {
		this.extraDayCharges = extraDayCharges;
	}

	public String getReceipt() {
		if(extraDayCharges == 0){
			return "No Dues";
		}
		return "You have to pay extra Day charge(s) "+extraDayCharges;
	}

}
