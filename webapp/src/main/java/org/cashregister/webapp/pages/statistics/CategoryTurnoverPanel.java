package org.cashregister.webapp.pages.statistics;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.cashregister.domain.Category;
import org.cashregister.webapp.component.CustomDropDownChoice;
import org.cashregister.webapp.pages.charts.AbstractCountPanel;
import org.cashregister.webapp.pages.charts.TimeGrouper;
import org.cashregister.webapp.persistence.api.StatisticsRepository;
import org.cashregister.webapp.service.api.CategoryService;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by derkhumblet on 02/04/16.
 */
public class CategoryTurnoverPanel extends AbstractCountPanel {
    private CustomDropDownChoice<Category> productCategory;
    private StatisticsRepository repository;
    private IModel<Category> selectedCategory;
    private long merchantId;

    @SpringBean
    private CategoryService categoryService;

    public CategoryTurnoverPanel(String id, StatisticsRepository repo, long merchantid, IModel<Date> startDate, IModel<Date> endDate, long merchantId, Locale locale) {
        super(id, merchantid, startDate, endDate, 1, locale);
        setOutputMarkupId(true);
        this.repository = repo;
        this.merchantId = merchantId;
        initCategorySelection();
    }

    @Override
    protected String getName() {
        return "categoryTurnoverChart";
    }

    private void initCategorySelection() {
        final CategoryTurnoverPanel thisRef = this;
        selectedCategory = Model.of(new Category());
        productCategory = new CustomDropDownChoice<>(
                "category",
                "Kies een categorie",
                selectedCategory,
                categoryService.getCategories(merchantId),
                new IChoiceRenderer<Category>() {
                    @Override
                    public Object getDisplayValue(Category object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(Category object, int index) {
                        return String.valueOf(object.getId());
                    }

                });

        productCategory.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(thisRef);
            }
        });
        productCategory.setNullValid(true);
        add(productCategory.setOutputMarkupId(true));
    }

    @Override
    protected Map<String, Float> getMainData() {
        TimeGrouper grouper = calculateGroupByTime();
        Map<String, Float> categorySaleTurnover = null;
        if (selectedCategory.getObject() != null && selectedCategory.getObject().getId() != null) {
            // Get sale turnover data per category
            categorySaleTurnover = repository.getCategoryTurnover(merchantId, selectedCategory.getObject().getId(), startDate.getObject(), endDate.getObject(), grouper);
        } else {
            categorySaleTurnover = new HashMap<>();
        }
        return fillMissingDatesFloat(categorySaleTurnover, grouper);
    }

    @Override
    protected Map<String, Float> getSecondaryData() {
        TimeGrouper grouper = calculateGroupByTime();
        Map<String, Float> categorySaleTurnover = null;
        if (selectedCategory.getObject() != null && selectedCategory.getObject().getId() != null) {
            // Get sale turnover data per category
            categorySaleTurnover = repository.getCategoryTransactionCount(merchantId, selectedCategory.getObject().getId(), startDate.getObject(), endDate.getObject(), grouper);
        } else {
            categorySaleTurnover = new HashMap<>();
        }
        return fillMissingDatesFloat(categorySaleTurnover, grouper);
    }
}
