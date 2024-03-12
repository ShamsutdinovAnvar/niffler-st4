package guru.qa.niffler.page.component;

import guru.qa.niffler.model.spend.SpendJson;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendCollectionCondition.spends;

public class SpendingTable extends BaseComponent<SpendingTable>{

    public SpendingTable() {
        super($(".spendings-table tbody"));
    }

    public SpendingTable checkSpends(SpendJson... expectedSpends) {
        getSelf()
                .scrollIntoView(true)
                .$$("tr").should(spends(expectedSpends));
        return this;
    }

    public SpendingTable selectByIndex(int index) {
        getSelf().$$("tr")
                .get(index-1)
                .$$("td")
                .first()
                .scrollIntoView(true)
                .click();
        return this;
    }

    public SpendingTable selectByText(String textVal) {
        getSelf().$$("tr")
                .find(text(textVal))
                .$$("td")
                .first()
                .scrollIntoView(true)
                .click();
        return this;
    }
}