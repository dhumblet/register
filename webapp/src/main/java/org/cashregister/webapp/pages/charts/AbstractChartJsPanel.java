package org.cashregister.webapp.pages.charts;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractChartJsPanel extends Panel {

	public AbstractChartJsPanel(String id) {
		super(id);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(getChartJs()));
	}

	private String getDataHeader() {
		switch (getChartType()) {
			case PIE:
			case DOUGHNUT:
			default:
				return "var data = [";
			case LINE:
				return "var data = {";
		}
	}

	private String getPieData(String label, float value, int index) {
		final String color = ChartColors.getColor(index).getColor();
		final String highlight = ChartColors.getColor(index).getHighlight();
		StringBuilder sb = new StringBuilder();
		if (index > 0) {
			sb.append(",");
		}

		sb.append("\n\t{")
			.append("\n\t\tvalue: ")
			.append(value).append(",")
			.append("\n\t\tcolor:").append("\"").append(color).append("\",")
			.append("\n\t\thighlight: ").append("\"").append(highlight).append("\",")
			.append("\n\t\tlabel: ").append("\"").append(label).append("\"")
		.append("\n\t}");
		return sb.toString();
	}

	private String getLineData() {
		StringBuilder sb = new StringBuilder();
		StringBuilder labels = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for (Map.Entry<String, Float> entry : getData().entrySet()) {
			labels.append("\"").append(entry.getKey()).append("\"").append(", ");
			values.append(entry.getValue()).append(", ");
		}

		sb.append("\nlabels : [").append(labels).append("],")
			.append("\ndatasets : [")
			.append("\n\t{")
				.append("\n\t\tfillColor : \"#94C3EA\",")
				.append("\n\t\tstrokeColor : \"#428bca\",")
				.append("\n\t\tpointColor : \"#428bca\",")
				.append("\n\t\tpointStrokeColor : \"#FFFFFF\",")
				.append("\n\t\tpointHighlightFill : \"#FFFFFF\",")
				.append("\n\t\tpointHighlightStroke : \"#0D8BAA\",")
				.append("\n\t\tdata : [").append(values).append("]")
				.append("\n\t}")
				.append("\n]");
		return sb.toString();
	}

	private String getDataFooter() {
		switch (getChartType()) {
		case PIE:
		case DOUGHNUT:
		default:
			return "]";
		case LINE:
			return "}";
		}
	}

	private String getOptionsJs() {
		StringBuilder sb = new StringBuilder("\nvar options = {");
		switch (getChartType()) {
		case PIE:
			break;
		case DOUGHNUT:
			sb.append("\n\tpercentageInnerCutout : 75");
			break;
		case LINE:
			sb
                .append("\n\tscaleShowGridLines : false,")
                .append("\n\tbezierCurve : false,")
                .append("\n\tdatasetFill : true,")
                .append("\n\tscaleBeginAtZero: true,");
			break;
		default:
			break;
		}
		sb.append("\n}");
		return sb.toString();
	}

	private String getCanvasJs() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nvar ").append(getName()).append("=document.getElementById('").append(getName()).append("').getContext('2d');")
                .append("\nnew Chart(").append(getName()).append(").")
                .append(getChartType().getName())
				.append("(data,options);");
		return sb.toString();
	}

	@Transactional
	private String getChartJs() {
		// Build chart.js javascript
		StringBuilder sb = new StringBuilder();
		sb.append(getDataHeader());
		// Add data
		switch (getChartType()) {
		case PIE:
		case DOUGHNUT:
		default:
			int count = 0;
			for (Map.Entry<String, Float> data : getDataSorted().entrySet()) {
				sb.append(getPieData(data.getKey(), data.getValue(), count));
				++count;
			}
			break;
		case LINE:
			sb.append(getLineData());
			break;
		}
		sb.append(getDataFooter());
		sb.append(getOptionsJs());
		sb.append(getCanvasJs());
		return sb.toString();
	}

	private Map<String, Float> getDataSorted() {
		Map<String, Float> data = getData();

		ValueComparableMap<String, Float> valueComparableMap = new ValueComparableMap<String, Float>(Ordering.<Float>natural().reverse());
		valueComparableMap.putAll(data);
		return Collections.unmodifiableMap(valueComparableMap);
	}

	/**
	 * @return the type of chart that will be generated.
	 */
	protected abstract ChartType getChartType();

	/**
	 * @return a map of the data that needs to be displayed.
	 */
	protected abstract Map<String, Float> getData();

	/**
	 * @return a unique name to identify this javascript variable.
	 */
	protected abstract String getName();

	public enum ChartType {
		PIE("Pie"), DOUGHNUT("Doughnut"), LINE("Line");

		private final String name;

		ChartType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	static class ValueComparableMap<K extends Comparable<K>, V> extends TreeMap<K, V> {
	    //A map for doing lookups on the keys for comparison so we don't get infinite loops
	    private final Map<K, V> valueMap;

	    ValueComparableMap(final Ordering<? super V> partialValueOrdering) {
			this(partialValueOrdering, new HashMap<K, V>());
	    }

	    private ValueComparableMap(Ordering<? super V> partialValueOrdering,
	            HashMap<K, V> valueMap) {
	        super(partialValueOrdering //Apply the value ordering
	                .onResultOf(Functions.forMap(valueMap)) //On the result of getting the value for the key from the map
	                .compound(Ordering.natural())); //as well as ensuring that the keys don't get clobbered
	        this.valueMap = valueMap;
	    }

	    @Override
		public V put(K k, V v) {
			if (valueMap.containsKey(k)) {
	            //remove the key in the sorted set before adding the key again
	            remove(k);
	        }
			valueMap.put(k, v); // To get "real" unsorted values for the comparator
	        return super.put(k, v); //Put it in value order
	    }
	 }

}
