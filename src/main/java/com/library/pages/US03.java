package com.library.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class US03 extends BasePage{



    @FindBy(xpath = "//*[@id=\"menu_item\"]/li[3]/a/span[1]")
    public WebElement BooksPageButton;


    @FindBy(id = "book_categories")
    public WebElement AllBookCategory;


    @FindBy(id = "book_categories")
    public WebElement categoryDropdown1;

    // Method to get dropdown options
    public List<String> getDropdownOptions() {
        Select dropdown = new Select(categoryDropdown1);

        List<WebElement> options = dropdown.getOptions();

        List<String> categories = new ArrayList<>();

        for (WebElement option : options) {
            String categoryName = option.getText().trim(); // Trim to handle any leading/trailing spaces

            // Skip adding the category "ALL"
            if (!categoryName.equalsIgnoreCase("ALL")) {
                categories.add(categoryName);
            }
        }

        return categories;
    }

}
