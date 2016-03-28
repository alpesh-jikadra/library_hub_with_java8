package com.library;


import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import com.library.exception.InvalidBookCategory;
import com.library.exception.InvalidBookLanguage;
import com.library.exception.InvalidMembershipPlan;
import com.library.exception.NoBookAvailable;

public class TestLibrary {

	private Library library = new Library();
	
	@Before
	public void setup() throws InvalidBookCategory, InvalidBookLanguage{
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
		Category arts = Category.getCategory("Arts").get();
		List<Book> collect = library.getBooks().stream().filter(b -> !b.getCategory().equals(arts)).collect(Collectors.toList());
		library.getBooks().clear();
		library.getBooks().addAll(collect);
		Member m  = library.issueMembership("Alpesh", "Ivory");
		Book b1 = new Book(Category.getCategory("Arts"), Language.getLanguage("English"),"1","Art Title");
		library.issueBook(m , b1);
		Assert.fail("This statement must not be exexute");
	}
	@Test
	public void sholdPrintReceiptOnBookReturn() throws InvalidMembershipPlan, InvalidBookCategory, InvalidBookLanguage, NoBookAvailable{
		Double seventyPerDay = 70d;
		Integer extraDay = 2;
		Library library = Mockito.spy(Library.class);
		Mockito.doCallRealMethod().when(library).issueMembership("Alpesh", "Ivory");
		Member member = Mockito.spy(library.issueMembership("Alpesh", "Ivory"));
		Optional<LibraryPlan> lb = LibraryPlan.getPlan("Ivory");
		LibraryPlan spy = Mockito.spy(lb.get());
		Book book = Mockito.mock(Book.class);
		Mockito.doNothing().when(library).issueBook(member, book);
		Mockito.when(member.getPlan()).thenReturn(Optional.of(spy));
		//set Extra day Charge regardless of plan
		Mockito.when(spy.getExtraDayCharges()).thenReturn(seventyPerDay);
		
		Calendar issueDate = Calendar.getInstance();
		issueDate.set(Calendar.DATE,issueDate.get(Calendar.DATE) - extraDay ); //Set 2 days in delay to return book
		Mockito.doReturn(issueDate).when(book).getIssueDate();
		
		Mockito.doCallRealMethod().when(library).returnBook(member, book);
		Receipt returnBook = library.returnBook(member, book);
		Assert.assertEquals("You have to pay extra Day charge(s) "+seventyPerDay*extraDay, returnBook.getReceipt());
		Calendar todayIssueDate = Calendar.getInstance();
		Mockito.doReturn(todayIssueDate).when(book).getIssueDate();
		returnBook = library.returnBook(member, book);
		Assert.assertEquals("No Dues", returnBook.getReceipt());
	}
	
	@Test
	public void shouldRestTheBookForNextIssue() throws NoBookAvailable, InvalidMembershipPlan, InvalidBookCategory, InvalidBookLanguage{
		Member m = library.issueMembership("Alpesh", "Ivory");
		Book b1 = new Book(Category.getCategory("Arts"), Language.getLanguage("English"),"1","Art Title");
		int beforeIssue = library.getAllAvailableBooksInLibrary().size();
		library.issueBook(m , b1);
		int totalBooksWithMember = m.getBooks().size();
		int afterIssue = library.getAllAvailableBooksInLibrary().size();
		Assert.assertEquals("Total book "+beforeIssue+" must be dedcuted by 1 ", beforeIssue -1 , afterIssue);
		library.returnBook(m, m.getBooks().get(0));
		int afterReturn = library.getAllAvailableBooksInLibrary().size();
		Assert.assertEquals("Total "+afterIssue+" book must be Increased by 1 ", afterIssue + 1 , afterReturn);
		Assert.assertEquals("After Returning the book member books collection must be deducted by 1", totalBooksWithMember- 1, m.getBooks().size());
	}
}
