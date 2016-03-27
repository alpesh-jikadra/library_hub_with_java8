package com.library;

import java.util.List;
import java.util.Optional;

public interface Member {

	String getMemberName() ;
	Optional<LibraryPlan> getPlan();
	Integer getMemberId() ;
	List<Book> getBooks();
}
