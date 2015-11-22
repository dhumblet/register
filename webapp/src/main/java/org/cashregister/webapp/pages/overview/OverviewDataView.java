package org.cashregister.webapp.pages.overview;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.cashregister.domain.OverviewLine;
import org.cashregister.webapp.component.CurrencyLabel;

/**
 * Created by derkhumblet on 02/03/15.
 */
public class OverviewDataView extends DataView<OverviewLine> {

    public OverviewDataView(String id, IDataProvider<OverviewLine> provider) {
        super(id, provider);
    }

    @Override
    protected void populateItem(Item<OverviewLine> item) {
        final OverviewLine overviewLine = item.getModelObject();
        item.add(new Label("product", overviewLine.name()));
        item.add(new Label("category", overviewLine.category()));
        item.add(new Label("amount", overviewLine.amount()));
        item.add(new CurrencyLabel("price", Model.of(overviewLine.price())));
    }
}
