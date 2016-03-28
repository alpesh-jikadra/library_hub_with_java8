package com.library;

import java.util.Calendar;
import java.util.Optional;

public class Book {

	private Category category;
	private Language language;
	private String version;
	private String title;
	private Integer bookId;
	private boolean availableInLibrary;
	private Calendar issueDate;
	public Book(Optional<Category> category , Optional<Language> language, String version, String title) {
		this.category = category.get();
		this.language = language.get();
		this.version = version;
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public Language getLanguage() {
		return language;
	}

	public String getVersion() {
		return version;
	}

	public String getTitle() {
		return title;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public boolean isAvailableInLibrary() {
		return availableInLibrary;
	}

	public void setAvailableInLibrary(boolean availableInLibrary) {
		this.availableInLibrary = availableInLibrary;
	}

	public Calendar getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Calendar issueDate) {
		this.issueDate = issueDate;
	}
	
}
