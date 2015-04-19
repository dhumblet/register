package org.cashregister.webapp.pages.overview;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.OverviewLine;
import org.cashregister.webapp.service.api.OverviewService;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by derkhumblet on 02/03/15.
 */
public class OverviewProvider implements IDataProvider<OverviewLine> {
    @SpringBean private OverviewService service;
    private long merchantId;
    private IModel<Date> start, end;
    final private boolean refunds, truck;

    public OverviewProvider(OverviewService service, IModel<Date> start, IModel<Date> end, long merchantId, boolean refunds, boolean truck) {
        this.service = service;
        this.merchantId = merchantId;
        this.start = start;
        this.end = end;
        this.truck = truck;
        this.refunds = refunds;
    }

    @Override
    public long size() {
        return service.countOverview(start.getObject(), end.getObject(), merchantId, refunds, truck);
    }

    @Override
    public Iterator<OverviewLine> iterator(long first, long count) {
        return service.getOverview(start.getObject(), end.getObject(), merchantId, refunds, truck).iterator();
    }

    @Override
    public IModel<OverviewLine> model(OverviewLine overviewLine) {
        return Model.of(overviewLine);
    }

    @Override
    public void detach() { }
}
