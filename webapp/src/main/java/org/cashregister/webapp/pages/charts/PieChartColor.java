package org.cashregister.webapp.pages.charts;

public class PieChartColor {
    private final String color;
    private final String highlight;

    public PieChartColor(String color, String highlight) {
        this.color = color;
        this.highlight = highlight;
    }

    public String getColor() {
        return color;
    }

    public String getHighlight() {
        return highlight;
    }
}