package com.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.library.exception.InvalidMembershipPlan;

public class LibraryPlan {

	private static final Map<String , LibraryPlan> plans = new HashMap<String, LibraryPlan>();
	
	private String planName;
	private Integer numberOfBooksAllowed;
	private Integer maxDaysForBook;
	private Double extraDayCharges;
	private Double packageAmount;
	
	static {
		LibraryPlan p = new LibraryPlan("Ivory", 3, 7, 20d, 1000d);
		plans.put(p.getPlanName(), p);
		
		p = new LibraryPlan("Silver", 4, 10, 30d, 2000d);
		plans.put(p.getPlanName(), p);
		
		p = new LibraryPlan("Gold", 5, 15, 40d, 3000d);
		plans.put(p.getPlanName(), p);
		
		p = new LibraryPlan("Platinum", 6, 20, 50d, 5000d);
		plans.put(p.getPlanName(), p);
		
	}
	
	private LibraryPlan(String planName, Integer numberOfBooksAllowed,
			Integer maxDaysForBook, Double extraDayCharges, Double packageAmount) {
		super();
		this.planName = planName;
		this.numberOfBooksAllowed = numberOfBooksAllowed;
		this.maxDaysForBook = maxDaysForBook;
		this.extraDayCharges = extraDayCharges;
		this.packageAmount = packageAmount;
	}

	public static Optional<LibraryPlan> getPlan(String plan) throws InvalidMembershipPlan{
		if(!plans.containsKey(plan)){
			throw new InvalidMembershipPlan("Membership plan "+plan +" is not valid");	
		}
		return Optional.of(plans.get(plan));
		
	}

	public String getPlanName() {
		return planName;
	}

	public Integer getNumberOfBooksAllowed() {
		return numberOfBooksAllowed;
	}

	public Integer getMaxDaysForBook() {
		return maxDaysForBook;
	}

	public Double getExtraDayCharges() {
		return extraDayCharges;
	}

	public Double getPackageAmount() {
		return packageAmount;
	}
	
}
