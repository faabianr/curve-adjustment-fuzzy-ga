package uag.mcc.ai.fuzzy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Curve {

    private String seriesName;
    private List<Double> xValues;
    private List<Double> yValues;

}
