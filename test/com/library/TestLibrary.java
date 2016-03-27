package com.library;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.library.exception.InvalidBookCategory;
import com.library.exception.InvalidBookLanguage;
import com.library.exception.InvalidMembershipPlan;
import com.library.exception.NoBookAvailable;

public class TestLibrary {

	private static Library library = new Library();
	
	@BeforeClass
	public static void setup() throws InvalidBookCategory, InvalidBookLanguage{
		Book b1 = new Book(Category.getCategory("Arts"), Language.getLanguage("English"),"1","Art Title");
		Assert.assertTrue(library.addBook(b1));
		Book b2 = new Book(Category.getCategory("Arts"), Language.getLanguage("Hindi"),"1","Art Title");
		Assert.assertTrue(library.addBook(b2));
		Book b3 = new Book(Category.getCategory("Business"), Language.getLanguage("English"),"1","Business Title");
		Assert.assertTrue(library.addBook(b3));
		Book b4 = new Book(Category.getCategory("Business"), Language.getLanguage("Hindi"),"1","Business Title");
		Assert.assertTrue(library.addBook(b4));
		Book b5 = new Book(Category.getCategory("Sports"), Language.getLanguage("English"),"1","Sport Title");
		Assert.assertTrue(library.addBook(b5));
		Book b6 = new Book(Category.getCategory("Sports"), Language.getLanguage("Hindi"),"1","SportTitle");
		Assert.assertTrue(library.addBook(b6));
		Book b7 = new Book(Category.getCategory("Cooking"), Language.getLanguage("English"),"1","Cooking Title");
		Assert.assertTrue(library.addBook(b7));
		Book b8 = new Book(Category.getCategory("Cooking"), Language.getLanguage("Hindi"),"1","Cooking Title");
		Assert.assertTrue(library.addBook(b8));
		Book b9 = new Book(Category.getCategory("History"), Language.getLanguage("English"),"1","History Title");
		Assert.assertTrue(library.addBook(b9));
		Book b10 = new Book(Category.getCategory("History"), Language.getLanguage("Hindi"),"1","History Title");
		Assert.assertTrue(library.addBook(b10));
	}
	@Test(expected=InvalidBookCategory.class)
	public void shouldValidateBookCategory() throws InvalidBookCategory, InvalidBookLanguage{
		new Book(Category.getCategory("Test"), Language.getLanguage("Hindi"),"1","Art Title");
	}
	@Test(expected=InvalidBookLanguage.class)
	public void shouldValidateLanguage() throws InvalidBookCategory, InvalidBookLanguage{
		new Book(Category.getCategory("Arts"), Language.getLanguage("Test"),"1","Art Title");
	}
	@Test
	public void shouldGetCategoryWiseBooks() throws InvalidBookCategory{
		Stream<Book> book = library.getBookByCategory(Category.getCategory("Arts"));
		Assert.assertNotNull(book);
		Assert.assertEquals(2, book.count());
	}
	@Test
	public void shouldReturnBooksBasedOnLanguages() throws InvalidBookLanguage{
		Stream<Book> bookByLanguage = library.getBookByLanguage(Language.getLanguage("Hindi"));
		Assert.assertEquals(5, bookByLanguage.count());
	}
	
	@Test
	public void shouldReturnBooksByCategoryAndLanguage() throws InvalidBookCategory, InvalidBookLanguage{
		Stream<Book> book = library.getBook(Category.getCategory("Arts").get(), Language.getLanguage("Hindi").get());
		Assert.assertEquals(1, book.count());
		
		List<Book> collect = library.getBooks().stream().collect(Collectors.toList());
		Library l = new Library();
		Category arts = Category.getCategory("Arts").get();
		collect.stream().filter(b -> !(b.getCategory().equals(arts))).forEach(b->l.addBook(b));
		book = l.getBook(Category.getCategory("Arts").get(), Language.getLanguage("Hindi").get());
		Assert.assertEquals(0, book.count());
	}
	
	@Test
	public void shouldAbleToIssueMembership() throws InvalidMembershipPlan{
		Member m  = library.issueMembership("Alpesh", "Ivory");
		Assert.assertNotNull(m);
		Assert.assertEquals("Alpesh",m.getMemberName());
		Assert.assertEquals("Ivory",m.getPlan().get().getPlanName());
	}
	
	@Test
	public void shouldIssueBookToMember() throws InvalidMembershipPlan, InvalidBookCategory, InvalidBookLanguage, NoBookAvailable{
		Member m  = library.issueMembership("Alpesh", "Ivory");
		Book b1 = new Book(Category.getCategory("Arts"), Language.getLanguage("English"),"1","Art Title");
		Integer beforeIssueCount = library.getAllAvailableBooksInLibrary().size();
		library.issueBook(m , b1);
		Assert.assertEquals(1, m.getBooks().size(), 0 );
		Integer afterIssueCount = library.getAllAvailableBooksInLibrary().size();
		Assert.assertEquals(afterIssueCount+1 , beforeIssueCount , 0 );
		library.addBook(b1);
	}
	@Test(expected=NoBookAvailable.class)
	public void shouldThrowBookNotAvailable() throws InvalidMembershipPlan, InvalidBookCategory, InvalidBookLanguage, NoBookAvailable{
		Library l = new Library();
		Category arts = Category.getCategory("Arts").get();
		l.getBooks().addAll(l.getBooks().stream().filter(b -> !b.getCategory().equals(arts)).collect(Collectors.toList()));
		Member m  = l.issueMembership("Alpesh", "Ivory");
		Book b1 = new Book(Category.getCategory("Arts"), Language.getLanguage("English"),"1","Art Title");
		l.issueBook(m , b1);
		Assert.fail("This statement must not be exexute");
	}
}
