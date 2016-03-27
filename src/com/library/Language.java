package com.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.exception.InvalidBookLanguage;

public class Language {

	private static final List<String> languages = new ArrayList<String>();
	private String language;
	static {
		languages.add("English");
		languages.add("Hindi");
		languages.add("French");
		languages.add("Spanish");
	}
	private Language(String language){
		this.language = language;
	}
	public static Optional<Language> getLanguage(String language) throws InvalidBookLanguage{
		if(!languages.contains(language)){
			throw new InvalidBookLanguage("Language "+language +" is not valid");
		}
		return Optional.of(new Language(language));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
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
		Language other = (Language) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		return true;
	}

	
}
