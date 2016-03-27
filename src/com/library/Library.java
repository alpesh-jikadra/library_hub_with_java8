package com.library;

import java.nio.channels.MembershipKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.library.exception.InvalidMembershipPlan;
import com.library.exception.NoBookAvailable;

public class Library {
	
	private List<Book> books= new ArrayList<Book>();
	private List<MemberImpl> members = new ArrayList<MemberImpl>();
	private static Integer mermberIdCounter = 0;
	private static Integer bookIdCounter = 0;
	public boolean addBook(Book b1) {
		b1.setBookId(++bookIdCounter);
		b1.setAvailableInLibrary(Boolean.TRUE);
		return this.books.add(b1);
	}

	public Stream<Book> getBookByCategory(Optional<Category> category) {
		Stream<Book> filter = books.stream().filter(b -> b.getCategory().equals(category.get()));
		return filter;
	}

	public Stream<Book> getBookByLanguage(Optional<Language> language) {
		Stream<Book> filter = books.stream().filter(b -> b.getLanguage().equals(language.get()));
		return filter;
	}

	public Stream<Book> getBook(Category category, Language language) {
		return books.stream().filter(b -> (b.getCategory().equals(category) && b.getLanguage().equals(language)));
	}

	public Member issueMembership(String memberName, String planName) throws InvalidMembershipPlan {
		
		MemberImpl memberImpl = new MemberImpl(memberName, LibraryPlan.getPlan(planName), mermberIdCounter++);
		this.members.add(memberImpl);
		return memberImpl;
		
	}

	
	public List<Book> getBooks() {
		return books;
	}
	public List<Book> getAllAvailableBooksInLibrary(){
		return books.stream().filter(b -> b.isAvailableInLibrary()).collect(Collectors.toList());
	}
	public Predicate<Book> getBooksByCategoryLanguageAndTitle(Book expectedBook){
		return b -> b.getCategory().equals(expectedBook.getCategory()) && 
				b.getLanguage().equals(expectedBook.getLanguage()) &&
				b.getTitle().equals(expectedBook.getTitle());
	}
	public List<Book> test(List<Book> books, Predicate<Book> p){
		return books.stream().filter(p).collect(Collectors.toList());
	}
	public void issueBook(Member m, Book b1) throws NoBookAvailable {
		MemberImpl m1 = (MemberImpl) m;
		Optional<Book> findFirst = books.stream().filter(getBooksByCategoryLanguageAndTitle(b1)).findFirst();
		if(!findFirst.isPresent()){
			throw new NoBookAvailable("Your Interested book is not available right now");
		}
		Book book = findFirst.get();
		int indexOf = this.books.indexOf(book);
		Book book2 = this.books.get(indexOf);
		book2.setAvailableInLibrary(Boolean.FALSE);
		m1.addBook(book);
	}

	private class MemberImpl implements Member {
		private String memberName;
		private Optional<LibraryPlan> plan;
		private Integer memberId;
		private List<Book> books = new ArrayList<Book>();
		
		public MemberImpl(String memberName, Optional<LibraryPlan> plan, Integer mermberId) {
			this.memberName = memberName;
			this.plan = plan;
			this.memberId = memberId;
		}
		public String getMemberName() {
			return memberName;
		}
		public Optional<LibraryPlan> getPlan() {
			return plan;
		}
		public Integer getMemberId() {
			return memberId;
		}
		public List<Book> getBooks() {
			return books;
		}
		
		public void addBook(Book b){
			this.books.add(b);
		}
		
	}


	
}
