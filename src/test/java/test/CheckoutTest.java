package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DbHelper;
import data.Utils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PurchasePage;

import static com.codeborne.selenide.Selenide.open;
import static data.DbHelper.getOrderCount;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutTest {

    private PurchasePage buy;
    String url = System.getProperty("sut.url");

    @BeforeEach
    public void openPage() {
        open(url);
        buy = new PurchasePage();
        buy.buyCard();

    }

    @BeforeAll
    static void setAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    public void cleanDataBase() {
        DbHelper.cleanDatabase();
    }

    @Test
    @DisplayName("01_Покупка тура картой со статусом APPROVED")
    public void shouldApproved() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.buySuccess();
        assertEquals("APPROVED", DbHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("02_Покупка тура картой со статусом DECLINED")
    public void shouldDeclined() {
        buy.setCardNumber(Utils.getDeclinedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.buyError();
        assertEquals("DECLINED", DbHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("03_Не заполнен номер карты")
    public void shouldErrNoNumberCard() {
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("04_Карта одобрена (статус APPROVED), не заполнен месяц")
    public void shouldErrNoMonth() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("05_Карта одобрена (статус APPROVED), не заполнен год")
    public void shouldErrNoYear() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("06_Карта одобрена (статус APPROVED), не заполнен Владелец")
    public void shouldErrNoOwner() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.incorrectFormatHidden();
        buy.fieldNecessarily();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("07_Карта одобрена (статус APPROVED), не заполнен код CVC")
    public void shouldErrNoCvc() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("08_Не корректный номер карты")
    public void shouldErrIncorrectNumberCard() {
        buy.setCardNumber(Utils.getCardNumber15Digits());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("09_Карта одобрена (статус APPROVED), срок карты истёк")
    public void shouldErrExpiredCard() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumberLessThanThisMonth());
        buy.setCardYear(Utils.getCurrentYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormatHidden();
        buy.cardExpirationError();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("10_Карта одобрена (статус APPROVED), не валидный месяц")
    public void shouldErrInvalidMonth() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getNumberFrom13To99());
        buy.setCardYear(Utils.getCurrentYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormatHidden();
        buy.cardExpirationError();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("11_Карта одобрена (статус APPROVED), некорректный месяц")
    public void shouldErrIncorrectMonth() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.get1Digit());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("12_Карта одобрена (статус APPROVED), некорректный год")
    public void shouldErrIncorrectYear() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.get1Digit());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("13_Карта одобрена (статус APPROVED), превышен срок карты")
    public void shouldErrInvalidDate() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getYearsAfterEndOfExpiration());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormatHidden();
        buy.cardExpirationError();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("14_Карта одобрена (статус APPROVED), некорректный Владелец")
    public void shouldErrIncorrectOwner() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getIncorrectCardHolder());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("15_Карта одобрена (статус APPROVED), короткое имя Владельца")
    public void shouldErrShortOwner() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getShotName());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("16_Карта одобрена (статус APPROVED), длинное имя Владельца")
    public void shouldErrLongOwner() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getLongName());
        buy.setCardCvv(Utils.get3Digits());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("17_Карта одобрена (статус APPROVED), некорректный код CVC - нули")
    public void shouldErrIncorrectCvcNulls() {
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get000());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

    @Test
    @DisplayName("18_Карта одобрена (статус APPROVED), некорректный код CVC - 1 цифра")
    public void shouldErrIncorrectCvcNotEnoughNumbers() {
        buy.setCardNumber(Utils.getApprovedCard());
        buy.setCardMonth(Utils.getMonthNumber());
        buy.setCardYear(Utils.getValidYear());
        buy.setCardholder(Utils.getNameCardholder());
        buy.setCardCvv(Utils.get1Digit());
        buy.clickContinueButton();
        buy.fieldNecessarilyHidden();
        buy.incorrectFormat();
        assertEquals(0, getOrderCount());
    }

}