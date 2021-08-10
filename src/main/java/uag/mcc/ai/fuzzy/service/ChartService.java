package uag.mcc.ai.fuzzy.service;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import uag.mcc.ai.fuzzy.model.ChartData;
import uag.mcc.ai.fuzzy.model.ChartStyleConfig;
import uag.mcc.ai.fuzzy.model.Curve;

import java.util.Arrays;

@Slf4j
public class ChartService {

    private SwingWrapper<XYChart> swingWrapper;

    public void displayCharts(ChartData tempMfChartData, ChartData tempChartData, ChartData rainMfChartData, ChartData rainChartData) {
        if (swingWrapper == null) {

            XYChart tempMfChart = buildChart(tempMfChartData);
            XYChart tempChart = buildChart(tempChartData);

            XYChart rainMfChart = buildChart(rainMfChartData);
            XYChart rainChart = buildChart(rainChartData);

            swingWrapper = new SwingWrapper<>(Arrays.asList(tempMfChart, tempChart, rainMfChart, rainChart));
            swingWrapper.setTitle("Curve Synthesis - Takagi Sugeno Network");
            swingWrapper.displayChartMatrix();
        }
    }

    private void applyChartStyle(XYChart chart, ChartStyleConfig styleConfig) {
        chart.getStyler().setMarkerSize(styleConfig.getMarkerSize());
        chart.getStyler().setChartTitleFont(styleConfig.getTitleFont());
        chart.getStyler().setLegendPosition(styleConfig.getLegendPosition());
        chart.getStyler().setSeriesMarkers(styleConfig.getSeriesMarkers());
        chart.getStyler().setToolTipsEnabled(styleConfig.isTooltipEnabled());
        chart.getStyler().setPlotGridLinesVisible(styleConfig.isPlotGridLinesVisible());
        chart.getStyler().setPlotBorderVisible(styleConfig.isPlotBorderVisible());
        chart.getStyler().setToolTipFont(styleConfig.getTooltipFont());
        chart.getStyler().setToolTipType(styleConfig.getToolTipType());
    }

    private XYChart buildChart(ChartData chartData) {
        XYChart chart = new XYChartBuilder()
                .theme(chartData.getStyleConfig().getTheme())
                .width(chartData.getStyleConfig().getWidth())
                .height(chartData.getStyleConfig().getHeight())
                .title(chartData.getTitle())
                .xAxisTitle(chartData.getXAxisTitle())
                .yAxisTitle(chartData.getYAxisTitle())
                .build();

        for (Curve curve : chartData.getCurves()) {
            chart.addSeries(curve.getSeriesName(), curve.getXValues(), curve.getYValues(), null);
        }

        applyChartStyle(chart, chartData.getStyleConfig());

        return chart;
    }

}
