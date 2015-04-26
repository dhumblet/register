package org.cashregister.webapp.pages.charts;

public enum TimeGrouper {
    HOURS("%Y%m%d%H"), DAYS("%Y%m%d"), WEEKS("%Y%u"), MONTHS("%Y%m"), YEARS("%Y");

    private String groupByTime;

    private TimeGrouper(String groupByTime) {
        this.groupByTime = groupByTime;
    }

    public String getGroupByTime() {
        return groupByTime;
    }
}
