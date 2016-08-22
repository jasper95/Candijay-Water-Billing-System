package com.charts.customizers;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;

import java.awt.Color;


/**
 * Created by jasper on 8/16/16.
 */
public class StackedBarCustomizer implements JRChartCustomizer {

    @Override
    public void customize(JFreeChart chart, JRChart jasperChart) {
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        map.mapKeyToGroup("Collection", "G1");
        map.mapKeyToGroup("Collectibles", "G2");
        map.mapKeyToGroup("Wage(1-15)", "G3");
        map.mapKeyToGroup("Wage(16-30)", "G3");
        map.mapKeyToGroup("Power Usage", "G3");
        renderer.setSeriesToGroupMap(map);
        renderer.setSeriesPaint(0, new Color(192,82,115), true);
        renderer.setSeriesPaint(1, new Color(64,176,240), true);
        renderer.setSeriesPaint(2, new Color(81,212,180), true);
        renderer.setSeriesPaint(3, new Color(223,197,77), true);
        renderer.setSeriesPaint(4, new Color(155,123,236), true);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(renderer);
    }

}
