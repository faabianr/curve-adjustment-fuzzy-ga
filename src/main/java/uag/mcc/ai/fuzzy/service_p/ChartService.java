package uag.mcc.ai.fuzzy.service_p;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;
import uag.mcc.ai.fuzzy.model_p.Chromosome;

import java.awt.*;
import java.util.List;
import java.util.*;

@Slf4j

public class ChartService {

    private static final String X = "x";
    private static final String Y = "y";

    private static final int CURVE_CHART_INDEX = 0;
    private static final String CURVE_CHART_TITLE = "Curve Adjustment";
    private static final String CURVE_CHART_SERIES_NAME_FOR_REFERENCE = "reference";
    private static final String CURVE_CHART_SERIES_NAME_FOR_APPROXIMATION = "approximation";

    private static final int APTITUDE_CHART_INDEX = 1;
    private static final String APTITUDE_CHART_TITLE = "Evolution of Aptitude Values";
    private static final String APTITUDE_CHART_SERIES_NAME = "Aptitude Function";
    private static final String APTITUDE_CHART_X_AXIS_TITLE = "Generation";
    private static final String APTITUDE_CHART_Y_AXIS_TITLE = "Best";
    private final Map<String, List<Double>> aptitudeChartValues;
    private double bestErrorFound;
    private SwingWrapper<XYChart> swingWrapper;
    private List<Double> bestCurveXValues;
    private List<Double> bestCurveYValues;

    public ChartService() {
        this.bestErrorFound = 0.0;
        this.aptitudeChartValues = new HashMap<>();
        this.aptitudeChartValues.put(X, new ArrayList<>());
        this.aptitudeChartValues.put(Y, new ArrayList<>());
        this.bestCurveXValues = new ArrayList<>();
        this.bestCurveYValues = new ArrayList<>();
    }

    private void updateAptitudeChart(Chromosome c) {
        if (bestErrorFound == 0) {
            bestErrorFound = c.getAptitude();
        } else if (c.getAptitude() < bestErrorFound) {
            bestErrorFound = c.getAptitude();
            bestCurveXValues = new ArrayList<>(c.getCurve().getXValues());
            bestCurveYValues = new ArrayList<>(c.getCurve().getYValues());
        }
    }

    public void displayCharts(Chromosome referenceChromosome, Chromosome approximationChromosome, int generationNumber) {
        aptitudeChartValues.get(X).add((double) generationNumber);
        aptitudeChartValues.get(Y).add(approximationChromosome.getAptitude());
        updateAptitudeChart(approximationChromosome);

        if (swingWrapper == null) {
            XYChart curveChart = new XYChartBuilder().theme(Styler.ChartTheme.Matlab)
                    .width(1200).height(600).title(CURVE_CHART_TITLE).xAxisTitle(X).yAxisTitle(Y).build();

            curveChart.addSeries(CURVE_CHART_SERIES_NAME_FOR_REFERENCE, referenceChromosome.getCurve().getXValues(), referenceChromosome.getCurve().getYValues(), null);
            curveChart.addSeries(CURVE_CHART_SERIES_NAME_FOR_APPROXIMATION, approximationChromosome.getCurve().getXValues(), approximationChromosome.getCurve().getYValues(), null);

            curveChart.getStyler().setChartTitleFont(new Font("Verdana", Font.PLAIN, 12));
            curveChart.getStyler().setSeriesLines(new BasicStroke[]{SeriesLines.SOLID});
            curveChart.getStyler().setPlotBorderVisible(false);

            XYChart aptitudeChart = new XYChartBuilder().theme(Styler.ChartTheme.Matlab)
                    .width(1200).height(600).title(APTITUDE_CHART_TITLE)
                    .xAxisTitle(APTITUDE_CHART_X_AXIS_TITLE).yAxisTitle(APTITUDE_CHART_Y_AXIS_TITLE).build();

            aptitudeChart.getStyler().setMarkerSize(5);
            aptitudeChart.getStyler().setChartTitleFont(new Font("Verdana", Font.PLAIN, 12));
            aptitudeChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            aptitudeChart.getStyler().setSeriesMarkers(new Marker[]{SeriesMarkers.CIRCLE});
            aptitudeChart.getStyler().setToolTipsEnabled(true);
            aptitudeChart.getStyler().setPlotGridLinesVisible(false);
            aptitudeChart.getStyler().setPlotBorderVisible(false);
            aptitudeChart.getStyler().setToolTipFont(new Font("Verdana", Font.PLAIN, 10));
            aptitudeChart.getStyler().setToolTipType(Styler.ToolTipType.xAndYLabels);

            aptitudeChart.addSeries(APTITUDE_CHART_SERIES_NAME, aptitudeChartValues.get(X), aptitudeChartValues.get(Y), null);

            swingWrapper = new SwingWrapper<>(Arrays.asList(curveChart, aptitudeChart));
            swingWrapper.displayChartMatrix();
        } else {
            updateBestAptitudeChart(approximationChromosome, generationNumber);
            updateCurveChart(
                    approximationChromosome.getCurve().getXValues(),
                    approximationChromosome.getCurve().getYValues(),
                    approximationChromosome.getAptitude(),
                    null,
                    generationNumber
            );

            swingWrapper.getXChartPanel().getChart().updateXYSeries(CURVE_CHART_SERIES_NAME_FOR_APPROXIMATION, approximationChromosome.getCurve().getXValues(), approximationChromosome.getCurve().getYValues(), null);
            swingWrapper.getXChartPanel().revalidate();
            swingWrapper.getXChartPanel().repaint();
        }

    }

    public void updateCurveChart(List<Double> xValues, List<Double> yValues, Double aptitude, String title, int generationNumber) {
        if (title == null) {
            title = String.format("Generation: %s, Best Aptitude Value: %s", generationNumber, aptitude);
        }

        swingWrapper.getXChartPanel(CURVE_CHART_INDEX).getChart()
                .updateXYSeries(
                        CURVE_CHART_SERIES_NAME_FOR_APPROXIMATION, xValues, yValues, null
                );

        swingWrapper.getXChartPanel(CURVE_CHART_INDEX).getChart().setTitle(title);
        swingWrapper.getXChartPanel(CURVE_CHART_INDEX).revalidate();
        swingWrapper.getXChartPanel(CURVE_CHART_INDEX).repaint();
    }

    public void updateBestAptitudeChart(Chromosome c, int generationNumber) {
        swingWrapper.getXChartPanel(APTITUDE_CHART_INDEX).getChart()
                .updateXYSeries(
                        APTITUDE_CHART_SERIES_NAME, aptitudeChartValues.get(X), aptitudeChartValues.get(Y), null
                );

        swingWrapper.getXChartPanel(APTITUDE_CHART_INDEX).getChart().setTitle(
                String.format("Generation: %s, Best Error of Generation: %s", generationNumber, c.getAptitude())
        );

        swingWrapper.getXChartPanel(APTITUDE_CHART_INDEX).revalidate();
        swingWrapper.getXChartPanel(APTITUDE_CHART_INDEX).repaint();
    }

    public void updateCurveChartWithBestOfGenerations() {
        String title = String.format("BestError: %s", bestErrorFound);
        updateCurveChart(bestCurveXValues, bestCurveYValues, bestErrorFound, title, 0);
    }

}
