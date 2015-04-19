package org.cashregister.webapp.pages.overview;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.component.CurrencyLabel;
import org.cashregister.webapp.component.CustomDateConverter;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.service.api.OverviewService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by derkhumblet on 02/03/15.
 */
public class OverviewPage extends SecureTemplatePage {
    @SpringBean
    private OverviewService service;
    private WebMarkupContainer container, salesTable, refundsTable, trucksTable;
    private OverviewDataView salesOverview, refundOverview, trucksOverview;
    private IModel<Date> startDate;
    private IModel<Date> endDate;
    private IModel<Date> searchBegin;
    private IModel<Date> searchFinal;
    private IModel<String> printableDates;
    private IModel<BigDecimal> salesTotal, refundsTotal, trucksTotal;
    public OverviewPage() { }

    @Override
    public String getTitle() {
        return getString("menu.stats");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));

        startDate = Model.of(new Date());
        endDate = Model.of(new Date());
        searchBegin = Model.of(getStart());
        searchFinal = Model.of(getEnd());
        printableDates = Model.of(dateToString(startDate.getObject()));
        salesTotal = Model.of(getOverviewTotal(false, false));
        refundsTotal = Model.of(getOverviewTotal(true, false));
        trucksTotal = Model.of(getOverviewTotal(false, true));

        searchBegin = Model.of(getStart());
        searchFinal = Model.of(getEnd());
        initPrintableDates();
        initSalesTable();
        initRefundsTable();
        initTrucksTable();
        initStartDatePicker();
        initEndDatePicker();
        initShowButtons();
    }

    private void initStartDatePicker() {
        //Date field
        DateTextField dateTextField = new DateTextField("startDate", startDate, new CustomDateConverter());

        DatePicker datePicker = new DatePicker();
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        dateTextField.add(datePicker);
        dateTextField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                //When the startdate is updated, set the enddate to the same day
                endDate.setObject(startDate.getObject());

                searchBegin.setObject(getStart());
                searchFinal.setObject(getEnd());
                calculatePrintableDates();
                salesTotal.setObject(getOverviewTotal(false, false));
                refundsTotal.setObject(getOverviewTotal(true, false));
                trucksTotal.setObject(getOverviewTotal(false, true));
                target.add(container);
            }
        });
        dateTextField.setMarkupId("statsstartdate");
        container.add(dateTextField.setOutputMarkupId(true));
    }

    private void initEndDatePicker() {
        //Date field
        DateTextField dateTextField = new DateTextField("endDate", endDate, new CustomDateConverter());

        DatePicker datePicker = new DatePicker();
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        dateTextField.add(datePicker);
        dateTextField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                searchBegin.setObject(getStart());
                searchFinal.setObject(getEnd());
                calculatePrintableDates();
                salesTotal.setObject(getOverviewTotal(false, false));
                refundsTotal.setObject(getOverviewTotal(true, false));
                trucksTotal.setObject(getOverviewTotal(false, true));
                target.add(container);
            }
        });
        dateTextField.setMarkupId("statsenddate");
        container.add(dateTextField.setOutputMarkupId(true));
    }

    private void initPrintableDates() {
        calculatePrintableDates();
        container.add(new Label("printableDates", printableDates));
    }

    private void calculatePrintableDates() {
        StringBuilder dates = new StringBuilder();
        dates.append(dateToString(startDate.getObject()));
        if (!sameDay()) {
            dates.append(" - ").append(dateToString(endDate.getObject()));
        }
        printableDates.setObject(dates.toString());
    }

    private boolean sameDay() {
        Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
        start.setTime(startDate.getObject());
        end.setTime(endDate.getObject());
        return start.get(Calendar.YEAR) == end.get(Calendar.YEAR)
                && start.get(Calendar.MONTH) == end.get(Calendar.MONTH)
                && start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    private void initSalesTable() {
        salesTable = new WebMarkupContainer("tableSales");
        container.add(salesTable.setOutputMarkupId(true));

        salesOverview = new OverviewDataView("salesOverview", new OverviewProvider(service, searchBegin, searchFinal, getMerchantId(), false, false));

        salesTable.add(salesOverview.setOutputMarkupId(true));
        salesTable.add(new CurrencyLabel("salesTotal", salesTotal));
    }

    private void initRefundsTable() {

        refundsTable = new WebMarkupContainer("tableRefunds");
        container.add(refundsTable.setOutputMarkupId(true));


        refundOverview = new OverviewDataView("refundOverview", new OverviewProvider(service, searchBegin, searchFinal, getMerchantId(), true, false));

        refundsTable.add(refundOverview.setOutputMarkupId(true));
        refundsTable.add(new CurrencyLabel("refundsTotal", refundsTotal));
    }

    private void initTrucksTable() {
        trucksTable = new WebMarkupContainer("tableTrucks");
        container.add(trucksTable.setVisible(false).setOutputMarkupId(true));

        trucksOverview = new OverviewDataView("trucksOverview", new OverviewProvider(service, searchBegin, searchFinal, getMerchantId(), false, true));

        trucksTable.add(trucksOverview.setOutputMarkupId(true));
        trucksTable.add(new CurrencyLabel("trucksTotal", trucksTotal));
    }

    private void initShowButtons() {
        Form showForm = new Form("showForm");
        showForm.add(new AjaxButton("showTrucks", showForm) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                trucksTable.setVisible(!trucksTable.isVisible());
                target.add(container);
            }
        }.setOutputMarkupPlaceholderTag(true));

        showForm.add(new AjaxButton("showRefunds", showForm) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                refundsTable.setVisible(!refundsTable.isVisible());
                target.add(container);
            }
        }.setOutputMarkupPlaceholderTag(true));
        container.add(showForm.setOutputMarkupId(true));
    }

    private Date getStart() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate.getObject());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate.getObject());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private BigDecimal getOverviewTotal(boolean refunds, boolean trucks) {
        return service.getOverviewTotal(searchBegin.getObject(), searchFinal.getObject(), getMerchantId(), refunds, trucks);
    }

    private String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy", getRegisterSession().getLocale());
        return df.format(date);
    }
}
