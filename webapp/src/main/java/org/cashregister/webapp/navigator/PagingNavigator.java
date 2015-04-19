package org.cashregister.webapp.navigator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

/**
 * Created by derkhumblet on 27/12/14.
 */
public class PagingNavigator extends AjaxPagingNavigator {

    private boolean autoHide;

    public PagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        super(id, pageable, labelProvider);
    }

    public PagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (autoHide) {
            setVisible(getPageable().getPageCount() > 1);
        }
    }

    @Override
    protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        return new AjaxPagingNavigation(id, pageable, labelProvider) {
            @Override
            protected void populateItem(LoopItem loopItem) {
                super.populateItem(loopItem);

                AjaxPagingNavigationLink pageLink = (AjaxPagingNavigationLink) loopItem.get("pageLink");
                loopItem.remove(pageLink);

                WebMarkupContainer pageContainer = new WebMarkupContainer("pageContainer");
                loopItem.add(pageContainer);
                pageContainer.add(pageLink);

                if (pageLink.getPageNumber() == pageable.getCurrentPage()) {
                    pageContainer.add(AttributeModifier.append("class", "active"));
                }
            }
        };
    }


    /**
     * Determine if the paging navigator should be hidden if
     * {@link IPageable#getPageCount()} > 1.
     *
     * @param value
     *            if true, the navigator is hidden if the pageable has pagecount less than 2
     * @return this for chaining
     */
    public PagingNavigator setAutoHide(boolean value) {
        this.autoHide = value;
        return this;
    }

}
