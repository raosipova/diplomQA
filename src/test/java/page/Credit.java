package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Credit {
    private final SelenideElement mainHead = $$("h2").find(exactText("Путешествие дня"));
    private final SelenideElement buyInCreditButton = $$("button").find(exactText("Купить в кредит"));
    private final SelenideElement buyInCreditHead = $$("h3").find(exactText("Кредит по данным карты"));
    private final SelenideElement cardNumber = $(byText("Номер карты")).parent().$("[class='input__control']");
    private final SelenideElement cardMonth = $(byText("Месяц")).parent().$("[class='input__control']");
    private final SelenideElement cardYear = $(byText("Год")).parent().$("[class='input__control']");
    private final SelenideElement cardOwner = $(byText("Владелец")).parent().$("[class='input__control']");
    private final SelenideElement cardCvc = $(byText("CVC/CVV")).parent().$("[class='input__control']");
    private final SelenideElement buySuccess = $(byText("Операция одобрена Банком.")).parent().$("[class='notification__content']");
    private final SelenideElement buyError = $(byText("Ошибка! Банк отказал в проведении операции.")).parent().$("[class='notification__content']");
    private final SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private final SelenideElement cardExpirationError = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement cardExpired = $(byText("Истёк срок действия карты"));
    private final SelenideElement fieldNecessarily = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement continueButton = $$("button").find(exactText("Продолжить"));

    public void buyCredit() {
        mainHead.shouldBe(visible);
        buyInCreditButton.click();
        buyInCreditHead.shouldBe(visible);
    }

    public void setCardNumber(String number) {
        cardNumber.setValue(number);
    }

    public void setCardMonth(String month) {
        cardMonth.setValue(month);
    }

    public void setCardYear(String year) {
        cardYear.setValue(year);
    }

    public void setCardholder(String user) {
        cardOwner.setValue(user);
    }

    public void setCardCvv(String cvc) {
        cardCvc.setValue(cvc);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void buySuccess() {
        buySuccess.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void buyError() {
        buyError.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void incorrectFormat() {
        incorrectFormat.shouldBe(visible);
    }

    public void incorrectFormatHidden() {
        incorrectFormat.shouldBe(hidden);
    }

    public void cardExpirationError() {
        cardExpirationError.shouldBe(visible);
    }

    public void cardExpired() {
        cardExpired.shouldBe(visible);
    }

    public void fieldNecessarily() {
        fieldNecessarily.shouldBe(visible);
    }

    public void fieldNecessarilyHidden() {
        fieldNecessarily.shouldBe(hidden);
    }

}