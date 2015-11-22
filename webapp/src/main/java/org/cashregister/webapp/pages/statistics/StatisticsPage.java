package org.cashregister.webapp.pages.statistics;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.webapp.component.CustomDateConverter;
import org.cashregister.webapp.pages.charts.ChartjsAjaxLazyLoadPanel;
import org.cashregister.webapp.pages.template.SecureTemplatePage;
import org.cashregister.webapp.persistence.api.StatisticsRepository;
import org.cashregister.webapp.util.DateHelper;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by derkhumblet on 25/04/15.
 */
public class StatisticsPage extends SecureTemplatePage {
    private WebMarkupContainer container;
    private IModel<Date> startDate, endDate;
    private AjaxLazyLoadPanel customerChartPanel, saleTurnoverPanel;
    @SpringBean private StatisticsRepository repo;

    public StatisticsPage() {}

    @Override
    public String getTitle() {
        return getString("menu.statistics");
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        container = new WebMarkupContainer("container");

        initDates();
        addCustomerCount();
        addSaleTurnover();

        add(container.setOutputMarkupId(true));
    }

    private void initDates() {
        startDate = Model.of(getFirstOfThisMonth());
        endDate = Model.of(getLastOfThisMonth());
        initDatePicker(true);
        initDatePicker(false);
        initButton(true);
        initButton(false);
        initShortcutButtons();
    }

    private void initDatePicker(final boolean start) {
        DateTextField dateTextField = new DateTextField(start ? "startDate" : "endDate",
                                                        start ? startDate : endDate,
                                                        new CustomDateConverter());
        DatePicker datePicker = new DatePicker();
        datePicker.setShowOnFieldClick(true);
        datePicker.setAutoHide(true);
        dateTextField.add(datePicker);
        dateTextField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (startDate.getObject().after(endDate.getObject())) {

                    if (start) {
                        //When the startdate is greater than the enddate, set the enddate to the same day
                        endDate.setObject(DateHelper.getEndOfDay(startDate.getObject()));
                    } else {
                        startDate.setObject(DateHelper.getStartOfDay(endDate.getObject()));
                    }
                }
                if (!start) {
                    endDate.setObject(DateHelper.getEndOfDay(endDate.getObject()));
                }
                target.add(container);
            }
        });
        dateTextField.setMarkupId(start ? "statsstartdate" : "statsenddate");
        container.add(dateTextField.setOutputMarkupId(true));
    }

    private void initButton(final boolean prev) {
        Form form = new Form(prev ? "prevForm" : "nextForm");
        form.add(new AjaxButton(prev ? "prevButton" : "nextButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                updateDates(prev);
                target.add(container);
            }
        }.setOutputMarkupId(true));
        container.add(form.setOutputMarkupId(true));
    }

    private void initShortcutButtons() {
        Form shortcutsForm = new Form("shortcutForm");
        shortcutsForm.add(new AjaxButton("shortcutToday", shortcutsForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                startDate.setObject(getFirstOfToday());
                endDate.setObject(getLastOfToday());
                target.add(container);
            }
        });
        shortcutsForm.add(new AjaxButton("shortcutWeek", shortcutsForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                startDate.setObject(getFirstOfThisWeek());
                endDate.setObject(getLastOfThisWeek());
                target.add(container);
            }
        });
        shortcutsForm.add(new AjaxButton("shortcutMonth", shortcutsForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                startDate.setObject(getFirstOfThisMonth());
                endDate.setObject(getLastOfThisMonth());
                target.add(container);
            }
        });
        shortcutsForm.add(new AjaxButton("shortcutAll", shortcutsForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                startDate.setObject(getFirstSaleDate());
                endDate.setObject(getLastOfToday());
                target.add(container);
            }
        });
        container.add(shortcutsForm.setOutputMarkupId(true));
    }

    private void addCustomerCount() {
        boolean replace = customerChartPanel != null;

        customerChartPanel = new ChartjsAjaxLazyLoadPanel("customerCountView") {
            @Override
            public Component doGetLazyLoadComponent(String id) {
                return new CustomerCountPanel(id, repo, getRegisterSession().getUser().getMerchant().getId(), startDate, endDate, getRegisterSession().getLocale());
            }
        };

        if (replace) {
            container.replace(customerChartPanel);
        } else {
            container.add(customerChartPanel);
        }
    }

    private void addSaleTurnover() {
        boolean replace = saleTurnoverPanel != null;

        saleTurnoverPanel = new ChartjsAjaxLazyLoadPanel("saleTurnoverView") {
            @Override
            public Component doGetLazyLoadComponent(String id) {
                return new SaleTurnoverPanel(id, repo, getRegisterSession().getUser().getMerchant().getId(), startDate, endDate, getRegisterSession().getLocale());
            }
        };

        if (replace) {
            container.replace(saleTurnoverPanel);
        } else {
            container.add(saleTurnoverPanel);
        }
    }

    /* Date helpers */

    private Date getFirstSaleDate() {
        return new DateMidnight().withDayOfMonth(1).withMonthOfYear(3).withYear(2015).toDate();
    }

    private Date getFirstOfThisMonth() {
        return new DateMidnight().withDayOfMonth(1).toDate();
    }

    private Date getLastOfThisMonth() {
        return new DateMidnight().withDayOfMonth(1).plusMonths(1).toDateTime().minusSeconds(1).toDate();
    }

    private Date getFirstOfThisWeek() {
        return new DateMidnight().withDayOfWeek(1).toDate();
    }

    private Date getLastOfThisWeek() {
        return new DateMidnight().withDayOfWeek(1).plusWeeks(1).toDateTime().minusSeconds(1).toDate();
    }

    private Date getFirstOfToday() {
        return new DateMidnight().toDate();
    }

    private Date getLastOfToday() {
        return new DateMidnight().plusDays(1).toDateTime().minusSeconds(1).toDate();
    }

    private void updateDates(boolean prev) {
        final DateTime start = new DateTime(startDate.getObject());
        final DateTime end = new DateTime(endDate.getObject());
        int days = differenceInDays(start, end);
        if (days >= 365 && sameYear(start, end)) {
            // Go to prev/next year
            if (prev) {
                startDate.setObject(start.minusYears(1).withDayOfYear(1).toDateMidnight().toDate());
                endDate.setObject(end.withDayOfYear(1).toDateMidnight().toDateTime().minusMillis(1).toDate());
            } else {
                startDate.setObject(start.plusYears(1).withDayOfYear(1).toDateMidnight().toDate());
                endDate.setObject(end.plusYears(2).withDayOfYear(1).toDateMidnight().toDateTime().minusMillis(1).toDate());
            }
        } else if (days >= 28 && sameMonth(start, end)) {
            // Go to prev/next month
            if (prev) {
                startDate.setObject(start.minusMonths(1).withDayOfMonth(1).toDateMidnight().toDate());
                endDate.setObject(end.withDayOfMonth(1).toDateMidnight().toDateTime().minusMillis(1).toDate());
            } else {
                startDate.setObject(start.plusMonths(1).withDayOfMonth(1).toDateMidnight().toDate());
                endDate.setObject(end.plusMonths(2).withDayOfMonth(1).toDateMidnight().toDateTime().minusMillis(1).toDate());
            }
        } else {
            // Switch with days interval
            if (prev) {
                startDate.setObject(start.minusDays(days).toDate());
                endDate.setObject(end.minusDays(days).toDate());
            } else {
                startDate.setObject(start.plusDays(days).toDate());
                endDate.setObject(end.plusDays(days).toDate());
            }
        }
    }

    private int differenceInDays(DateTime start, DateTime end) {
        return Days.daysBetween(start, end).getDays() + 1;
    }

    private boolean sameMonth(DateTime start, DateTime end) {
        return start.getMonthOfYear() == end.getMonthOfYear();
    }

    private boolean sameYear(DateTime start, DateTime end) {
        return start.getYear() == end.getYear();
    }

    /* Import Chart.js*/

    @Override
    protected List<String> getPageSpecificJavaImports() {
        return Arrays.asList("js/chart.min.js");
    }
}
