package com.library.steps;

import com.library.pages.*;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class USStep_Definition {

    String actualUserCount;
    US03 us03 = new US03();
    BookPage bookPage = new BookPage();
    DashBoardPage dashBoardPage = new DashBoardPage();
    BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage();

    LoginPage loginPage = new LoginPage();
    int actualBorrowedBooksNumbers;
    List<String> actualNameColumns = new ArrayList<>();
    List <String> actualBookCatgeory= new ArrayList<>();

    @Given("Establish the database connection")
    public void establish_the_database_connection() {
        //Make DB Connection
         DB_Util.createConnection();


    }
    @When("Execute query to get all IDs from users")
    public void execute_query_to_get_all_i_ds_from_users() {

        String query="select count(id) from users";
        DB_Util.runQuery(query);

        actualUserCount = DB_Util.getFirstRowFirstColumn();
        System.out.println(actualUserCount);

    }
    @Then("verify all users has unique ID")
    public void verify_all_users_has_unique_id() {
        String query="select count(distinct id) from users";
        DB_Util.runQuery(query);

        String expectedUserCount = DB_Util.getFirstRowFirstColumn();
        System.out.println(expectedUserCount);

        Assert.assertEquals(expectedUserCount,actualUserCount);

        // Close Coneection
        DB_Util.destroy();

    }

    @When("Execute query to get all columns")
    public void executeQueryToGetAllColumns() {
        DB_Util.createConnection();
        DB_Util.runQuery("SELECT * from users");
        actualNameColumns =  DB_Util.getAllColumnNamesAsList();
        System.out.println("actualNameColumns = " + actualNameColumns);

    }

    @Then("verify the below columns are listed in result")
    public void verifyTheBelowColumnsAreListedInResult(List<String> expectedColumns) {
        Assert.assertEquals(expectedColumns, actualNameColumns);

        DB_Util.destroy();
    }


    @Given("the {string} on the home page")
    public void theOnTheHomePage(String librarian) {

        loginPage.login(librarian);
    }
    String expectedBorrowedBook;
    String actualBookBorrowedBook;
    @When("the librarian gets borrowed books number")
    public void theLibrarianGetsBorrowedBooksNumber() {
        expectedBorrowedBook= dashBoardPage.borrowedBooksNumber.getText();
        DB_Util.createConnection();
        String query ="SELECT COUNT(*) from book_borrow where is_returned=0";
        DB_Util.runQuery(query);
        actualBookBorrowedBook=DB_Util.getFirstRowFirstColumn();

    }

    @Then("borrowed books number information must match with DB")
    public void borrowedBooksNumberInformationMustMatchWithDB() {

        Assert.assertEquals(actualBookBorrowedBook,expectedBorrowedBook);

        DB_Util.destroy();
    }

    @When("the user navigates to {string} page")
    public void theUserNavigatesToPage(String books) {

        dashBoardPage.navigateModule(books);
    }
    List<String> expectedbookCaategories = new ArrayList<>();
    @And("the user clicks book categories")
    public void theUserClicksBookCategories() {
        expectedbookCaategories= us03.getDropdownOptions();
        System.out.println("expectedbookCaategories = " + expectedbookCaategories);
    }

    @Then("verify book categories must match book_categories table from db")
    public void verifyBookCategoriesMustMatchBook_categoriesTableFromDb() {
        DB_Util.createConnection();
        DB_Util.runQuery("select name from book_categories");

        List<String > actualBookCategory=DB_Util.getColumnDataAsList("name");

        Assert.assertEquals(expectedbookCaategories,actualBookCategory);


    }
    String searchedBookName;
    @When("the user searches for {string} book")
    public void theUserSearchesForBook(String cleanCode) {
        bookPage.search.sendKeys(cleanCode);
        searchedBookName=cleanCode;



    }

    @And("the user clicks edit book button")
    public void theUserClicksEditBookButton() {
        bookPage.editBook(searchedBookName).click();
    }

    @Then("book information must match the Database")
    public void bookInformationMustMatchTheDatabase() {
        DB_Util.createConnection();
        String runQuery=("select * from books where name = 'Clean Code';");
        DB_Util.runQuery(runQuery);

        Map<String, String> DB_Book= DB_Util.getRowMap(1);

        String actuaBookUI= bookPage.bookName.getAttribute("value");
        String expectedDBBook= DB_Book.get("name");

        String actualIsbnUI=bookPage.isbn.getAttribute("value");
        String expectedIsbnDB=DB_Book.get("isbn");

        String actuaauthorUI= bookPage.author.getAttribute("value");
        String expectedAuthorDB= DB_Book.get("author");

        String actualYearUI=bookPage.year.getAttribute("value");
        String expectedYearDB=DB_Book.get("year");

        Assert.assertEquals(actuaBookUI,expectedDBBook);

        Assert.assertEquals(actualYearUI,expectedYearDB);

        Assert.assertEquals(actuaauthorUI,expectedAuthorDB);

        Assert.assertEquals(actualIsbnUI,expectedIsbnDB);


    }
    String MostPopularBookDB;
    @When("I execute query to find most popular book genre")
    public void iExecuteQueryToFindMostPopularBookGenre() {
        DB_Util.createConnection();
        String query="select name from book_categories\n" +
                "                where id = (select book_category_id from books\n" +
                "                where id = (select book_id from book_borrow group by book_id order by count(*) desc limit 1));";
        DB_Util.runQuery(query);

        MostPopularBookDB =DB_Util.getFirstRowFirstColumn();

    }

    @Then("verify {string} is the most popular book genre.")
    public void verifyIsTheMostPopularBookGenre(String bookPopularUI) {

        Assert.assertEquals(bookPopularUI,MostPopularBookDB);

        DB_Util.destroy();
    }

    @When("the librarian click to add book")
    public void theLibrarianClickToAddBook() {
        bookPage.addBook.click();

    }

    @And("the librarian enter book name {string}")
    public void theLibrarianEnterBookName(String bookName) {
        bookPage.bookName.sendKeys(bookName);
    }

    @When("the librarian enter ISBN {string}")
    public void theLibrarianEnterISBN(String isbn) {
        bookPage.isbn.sendKeys(isbn);
    }

    @And("the librarian enter year {string}")
    public void theLibrarianEnterYear(String year) {
        bookPage.year.sendKeys(year);
    }

    @When("the librarian enter author {string}")
    public void theLibrarianEnterAuthor(String author) {
        bookPage.author.sendKeys(author);

    }

    @And("the librarian choose the book category {string}")
    public void theLibrarianChooseTheBookCategory(String BookCategory) {
        bookPage.categoryDropdown.sendKeys(BookCategory);
    }

    @And("the librarian click to save changes")
    public void theLibrarianClickToSaveChanges() {
        bookPage.saveChanges.click();
    }

    @Then("verify {string} message is displayed")
    public void verifyMessageIsDisplayed(String massage) {
        String actualmsg=bookPage.toastMessage.getText();
        Assert.assertEquals(massage,actualmsg);

    }

    @And("verify {string} information must match with DB")
    public void verifyInformationMustMatchWithDB(String bookInfo) {

        DB_Util.createConnection();

        String query = "select name, author, isbn from books\n" +
                "where name = '"+bookInfo+"'";
        DB_Util.runQuery(query);
        Map<String, String> rowMap = DB_Util.getRowMap(1);

        String actualBookName = rowMap.get("name");

        Assert.assertEquals(bookInfo,actualBookName);




    }

    @And("the user searches for {string} books")
    public void theUserSearchesForBooks(String bookname) {
        bookPage.search.sendKeys(bookname);
    }

    @When("the user clicks Borrow Book")
    public void theUserClicksBorrowBook() {
        dashBoardPage.navigateModule("Borrow Book");
    }
    int bookIndex;
    @Then("verify that book is shown in {string} page")
    public void verifyThatBookIsShownInPage(String BorrowedbookPage) {
        bookPage.navigateModule(BorrowedbookPage);

        List <String> allReturnedBookListNumber= BrowserUtil.getElementsText(bookPage.returnBookValues(searchedBookName));

        bookIndex = allReturnedBookListNumber.size();
        //Assert.assertTrue();
    }

    @And("verify logged student has same book in database")
    public void verifyLoggedStudentHasSameBookInDatabase() {

        String query1 = "select email from users where id = (select user_id from book_borrow where book_id = 22060 order by id desc limit 1);";
        DB_Util.runQuery(query1);
        String actualUserLogged = DB_Util.getFirstRowFirstColumn();
        String expectedUserLogged = "student6@library";

        Assert.assertEquals(actualUserLogged,expectedUserLogged);

        WebElement returnBook=Driver.getDriver().findElement(By.xpath("(//td[.='"+ searchedBookName +"']/../td/a)["+bookIndex+"]"));
        returnBook.click();


    }




}
