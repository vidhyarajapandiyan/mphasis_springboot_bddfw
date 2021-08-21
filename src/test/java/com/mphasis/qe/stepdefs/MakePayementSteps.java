package com.mphasis.qe.stepdefs;

import com.mphasis.qe.pages.Native_DashboardPage;
import com.mphasis.qe.pages.Native_LoginPage;
import com.mphasis.qe.pages.Native_PaymentPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

/****************************************************************************************
 * @author Tamilselvan Ramalingam
 ****************************************************************************************/
@Slf4j
public class MakePayementSteps {
    private Native_LoginPage loginPage;
    private Native_PaymentPage paymentPage;
    private Native_DashboardPage dashboardPage;

    public MakePayementSteps() {
        this.loginPage = new Native_LoginPage();
        this.paymentPage = new Native_PaymentPage();
    }

    @Given("User logged into TestBank")
    public void userLoggedIntoTestBank() {
        this.loginPage.checkLogoDisplay();
        this.loginPage.enterUserName("test");
        this.loginPage.enterPassword("test");
        this.dashboardPage = this.loginPage.login();
    }

    @And("User lands on Dashboard page")
    public void userAmOnDashboardPage() {
        this.dashboardPage.checkBalanceDisplay();
    }

    @When("User clicks on {string}")
    public void userClicksOn(String link) {
        this.dashboardPage.navigateTo(link);
    }

    @Then("User lands on Payments page")
    public void userLandsOnPaymentsPage() {
    }

    @And("User enters {string} {string} {string}")
    public void userEnters(String phoneNumber, String name, String amount) {
        this.paymentPage.sendText("PhoneNumber", phoneNumber);
        this.paymentPage.sendText("Name", name);
        this.paymentPage.sendText("Amount", amount);
        log.info("Completed the User Payee Information");
    }

    @And("User selects {string}")
    public void userSelects(String country) {
        this.paymentPage.getCountrySelected(country);
    }

    @When("User clicks on SendPayment")
    public void userClicksOnSendPayment() {
        this.paymentPage.sendAction("Payment");

    }

    @Then("User able to make payment Successfully")
    public void userAbleToSendThePayment() {
        this.paymentPage.sendAction("Yes");
        userAmOnDashboardPage();
        log.info("Payment sent Successfully");
    }

}
