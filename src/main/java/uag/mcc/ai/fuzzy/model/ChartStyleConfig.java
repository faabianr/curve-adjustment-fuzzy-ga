package uag.mcc.ai.fuzzy.model;

import lombok.Builder;
import lombok.Data;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;

@Builder
@Data
public class ChartStyleConfig {

    @Builder.Default
    private int width = 1200;
    @Builder.Default
    private int height = 600;
    @Builder.Default
    private Styler.ChartTheme theme = Styler.ChartTheme.Matlab;
    @Builder.Default
    private BasicStroke[] seriesLines = new BasicStroke[]{SeriesLines.SOLID};
    @Builder.Default
    private boolean plotBorderVisible = false;
    @Builder.Default
    private int markerSize = 5;
    @Builder.Default
    private Styler.LegendPosition legendPosition = Styler.LegendPosition.InsideNE;
    @Builder.Default
    private boolean tooltipEnabled = false;
    @Builder.Default
    private boolean plotGridLinesVisible = false;
    @Builder.Default
    private Font titleFont = new Font("Verdana", Font.PLAIN, 12);
    @Builder.Default
    private Font tooltipFont = new Font("Verdana", Font.PLAIN, 10);
    @Builder.Default
    private Styler.ToolTipType toolTipType = Styler.ToolTipType.xAndYLabels;
    @Builder.Default
    private Marker[] seriesMarkers = new Marker[]{SeriesMarkers.NONE};

}
