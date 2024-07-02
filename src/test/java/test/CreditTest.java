package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DbHelper;
import data.Utils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.Credit;

import static com.codeborne.selenide.Selenide.open;
import static data.DbHelper.getOrderCount;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {

    private Credit buyInCredit;

    String url = System.getProperty("sut.url");

    @BeforeEach
    public void openPage() {
        open(url);
        buyInCredit = new Credit();
        buyInCredit.buyCredit();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    public void cleanDataBase() {
        DbHelper.cleanDatabase();
    }

    @Test
    @DisplayName("01_Покупка тура в кредит со статусом APPROVED")
    public void shouldApproved() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.buySuccess();
        assertEquals("APPROVED", DbHelper.getCreditStatus());
    }

    @Test
    @DisplayName("02_Покупка тура в кредит со статусом DECLINED")
    public void shouldDeclined() {
        buyInCredit.setCardNumber(Utils.getDeclinedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.buyError();
        assertEquals("DECLINED", DbHelper.getCreditStatus());
    }

    @Test
    @DisplayName("03_Не заполнен номер карты")
    public void shouldErrNoNumberCard() {
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("04_Карта одобрена (статус APPROVED), не заполнен месяц")
    public void shouldErrNoMonth() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("05_Карта одобрена (статус APPROVED), не заполнен год")
    public void shouldErrNoYear() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("06_Карта одобрена (статус APPROVED), не заполнен Владелец")
    public void shouldErrNoOwner() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.incorrectFormatHidden();
        buyInCredit.fieldNecessarily();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("07_Карта одобрена (статус APPROVED), не заполнен код CVC")
    public void shouldErrNoCvc() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("08_Не корректный номер карты")
    public void shouldErrIncorrectNumberCard() {
        buyInCredit.setCardNumber(Utils.getCardNumber15Digits());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("09_Карта одобрена (статус APPROVED), срок карты истёк")
    public void shouldErrExpiredCard() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getYearNumberLessCurrentYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormatHidden();
        buyInCredit.cardExpired();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("10_Карта одобрена (статус APPROVED), не валидный месяц")
    public void shouldErrInvalidMonth() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.get00());
        buyInCredit.setCardYear(Utils.getCurrentYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormatHidden();
        buyInCredit.cardExpirationError();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("11_Карта одобрена (статус APPROVED), некорректный месяц")
    public void shouldErrIncorrectMonth() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.get1Digit());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("12_Карта одобрена (статус APPROVED), некорректный год")
    public void shouldErrIncorrectYear() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.get1Digit());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("13_Карта одобрена (статус APPROVED), превышен срок карты")
    public void shouldErrInvalidDate() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getYearsAfterEndOfExpiration());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormatHidden();
        buyInCredit.cardExpirationError();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("14_Карта одобрена (статус APPROVED), некорректный Владелец")
    public void shouldErrIncorrectOwner() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholderWithCyrillic());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("15_Карта одобрена (статус APPROVED), короткое имя Владельца")
    public void shouldErrShortOwner() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getShotName());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("16_Карта одобрена (статус APPROVED), длинное имя Владельца")
    public void shouldErrLongOwner() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getLongName());
        buyInCredit.setCardCvv(Utils.get3Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("17_Карта одобрена (статус APPROVED), некорректный код CVC - нули")
    public void shouldErrIncorrectCvcNulls() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get000());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("18_Карта одобрена (статус APPROVED), некорректный код CVC - 2 цифры")
    public void shouldErrIncorrectCvcNotEnoughNumbers() {
        buyInCredit.setCardNumber(Utils.getApprovedCard());
        buyInCredit.setCardMonth(Utils.getMonthNumber());
        buyInCredit.setCardYear(Utils.getValidYear());
        buyInCredit.setCardholder(Utils.getNameCardholder());
        buyInCredit.setCardCvv(Utils.get2Digits());
        buyInCredit.clickContinueButton();
        buyInCredit.fieldNecessarilyHidden();
        buyInCredit.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

}