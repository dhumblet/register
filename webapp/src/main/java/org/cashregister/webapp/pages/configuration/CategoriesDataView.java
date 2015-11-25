//package org.cashregister.webapp.pages.configuration;
//
//import org.apache.wicket.AttributeModifier;
//import org.apache.wicket.ajax.AjaxRequestTarget;
//import org.apache.wicket.ajax.markup.html.form.AjaxButton;
//import org.apache.wicket.markup.html.WebMarkupContainer;
//import org.apache.wicket.markup.html.basic.Label;
//import org.apache.wicket.markup.html.form.Form;
//import org.apache.wicket.markup.html.form.TextField;
//import org.apache.wicket.markup.repeater.Item;
//import org.apache.wicket.markup.repeater.data.DataView;
//import org.apache.wicket.markup.repeater.data.IDataProvider;
//import org.apache.wicket.model.IModel;
//import org.apache.wicket.model.Model;
//import org.apache.wicket.model.PropertyModel;
//import org.cashregister.domain.Category;
//import org.cashregister.webapp.service.api.CategoryService;
//
///**
// * Created by derkhumblet on 07/06/15.
// */
//public class CategoriesDataView extends DataView<Category> {
//    private IModel<CategoriesModel> model;
//    private WebMarkupContainer container;
//    private AjaxButton editSave, delete;
//    private ConfigurationPageCallback callback;
//    private CategoryService service;
//    private TextField categoryName;
//
//    public CategoriesDataView(String id,
//                            IDataProvider<Category> provider,
//                            IModel<CategoriesModel> model,
//                            WebMarkupContainer container,
//                            CategoryService service,
//                            ConfigurationPageCallback callback) {
//        super(id, provider);
//        this.model = model;
//        this.container = container;
//        this.service = service;
//        this.callback = callback;
//    }
//    @Override
//    protected void populateItem(Item<Category> item) {
//        IModel<Category> categoryModel = Model.of(item.getModelObject());
//
//        categoryName = new TextField("name", new PropertyModel(categoryModel, "name"));
//        item.add(categoryName.setOutputMarkupId(true));
//
//        Form editForm = new Form("editForm");
//        populateEditButton(item, editForm, categoryModel);
//        populateDeleteButton(item, editForm, categoryModel);
//        item.add(editForm.setOutputMarkupId(true));
//
//        if (model.getObject().getEditCategoryId() != null && model.getObject().getEditCategoryId().longValue() == item.getModelObject().id().longValue()) {
//
//        } else {
//            categoryName.setEnabled(false);
//            StringBuilder style = new StringBuilder();
//            style.append("border: none;")
//                    .append("border-width:0px;")
//                    .append("cursor: initial;")
////                    .append("background-color: #f9f9f9;")
//            ;
//
//            categoryName.add(new AttributeModifier("style", style.toString()));
//            categoryName.add(new AttributeModifier("class", ""));
//        }
//    }
//
//    private void populateEditButton(Item<Category> item, Form editForm, final IModel<Category> productModel) {
//        final Category category = item.getModelObject();
//
//        editForm.add(new AjaxButton("edit", editForm) {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                super.onSubmit(target, form);
//                model.getObject().setEditCategoryId(category.id());
//                target.add(container);
//                callback.edit(target);
//            }
//        });
//    }
//
//    private void populateDeleteButton(Item<Category> item, Form editForm, IModel<Category> productModel) {
//        final Category category = item.getModelObject();
//        editForm.add(new AjaxButton("remove", editForm) {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                super.onSubmit(target, form);
//                service.delete(category);
//                target.add(container);
//            }
//        });
//    }
//}
