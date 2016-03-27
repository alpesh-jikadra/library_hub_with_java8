package com.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.exception.InvalidBookCategory;

public class Category {

	private static final List<String> categories = new ArrayList<String>();
	private String category;
	
	static {
		categories.add("Arts");
		categories.add("Business");
		categories.add("Sports");
		categories.add("Cooking");
		categories.add("History");
	}
	private Category(String category){
		this.category = category;
	}
	public static Optional<Category> getCategory(String category) throws InvalidBookCategory{
		if(!categories.contains(category)){
			throw new InvalidBookCategory("category "+category +" is not valid");	
		}
		return Optional.of(new Category(category));
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}

	
	
}
