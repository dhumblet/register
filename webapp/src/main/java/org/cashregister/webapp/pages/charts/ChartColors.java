package org.cashregister.webapp.pages.charts;

import java.util.ArrayList;
import java.util.List;

public final class ChartColors {

	private ChartColors() {
	}

    private static List<PieChartColor> colors;

    static {
        colors = new ArrayList<>();
        colors.add(new PieChartColor("#0DAA94", "#10CCB2")); // Green
        colors.add(new PieChartColor("#0DAA5F", "#00B75F")); // Green
        colors.add(new PieChartColor("#0D8BAA", "#0092B7")); // Blue
    	colors.add(new PieChartColor("#46BFBD", "#14F0ED")); // Turqoise
        colors.add(new PieChartColor("#F71329", "#FE0B22")); // Red
        colors.add(new PieChartColor("#FF8B00", "#FF8A00")); // Brown-Yellow
        colors.add(new PieChartColor("#396AB1", "#0A61DF")); // Blue
        colors.add(new PieChartColor("#6B4C9A", "#611EC8")); // Purple
        colors.add(new PieChartColor("#3E9651", "#13C038")); // Green
        colors.add(new PieChartColor("#F71329", "#FE0B22")); // Red
        colors.add(new PieChartColor("#5B5B5B", "#808585")); // Dark Grey
        colors.add(new PieChartColor("#1D54B2", "#004CCF")); // Dark Blue
        colors.add(new PieChartColor("#FF7513", "#FE7513")); // Dark Orange
        colors.add(new PieChartColor("#C99FC2", "#E781D6")); // Pink Grey
        colors.add(new PieChartColor("#ACC26D", "#BFEB43")); // Soft Green
        colors.add(new PieChartColor("#AA0D22", "#B70018")); // Red Brown
        colors.add(new PieChartColor("#AA450D", "#B74100")); // Brown
        colors.add(new PieChartColor("#940DAA", "#9D00B7")); // Light purple
        colors.add(new PieChartColor("#AA940D", "#B79D00")); // Olive green
    }

    public static PieChartColor getColor(int i) {
        return colors.get(i % colors.size());
    }

}
