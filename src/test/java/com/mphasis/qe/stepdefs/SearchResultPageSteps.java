package com.mphasis.qe.stepdefs;

import com.mphasis.qe.pageobjects.SearchResultPage;
import io.cucumber.java.en.Then;
/****************************************************************************************
 * @author manoj chavan
 ****************************************************************************************/
public class SearchResultPageSteps {

    private SearchResultPage searchResultPage;

    public SearchResultPageSteps() {
        this.searchResultPage = new SearchResultPage();
    }

    @Then("^\"([^\"]*)\" is displayed in the first \"([^\"]*)\" results$")
    public void isDisplayedInTheFirstResults(String expectedResultUrl, int nbOfResultsToSearch) {
        this.searchResultPage.checkExpectedUrlInResults(expectedResultUrl, nbOfResultsToSearch);
    }
}
