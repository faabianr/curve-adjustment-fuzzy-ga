package uag.mcc.ai.fuzzy.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;

@Data
@Builder
@ToString
public class Chromosome {

    private static final int TOTAL_POINTS = 120;

    private final double m1;
    private final double m2;
    private final double m3;
    private final double de1;
    private final double de2;
    private final double de3;
    private final double p1;
    private final double p2;
    private final double p3;
    private final double q1;
    private final double q2;
    private final double q3;

    private Curve referenceCurve;
    private Curve curve;
    private Curve mf1Curve;
    private Curve mf2Curve;
    private Curve mf3Curve;
    private Double aptitude;

    public void evaluate() {
        Double[] x = new Double[TOTAL_POINTS];
        Double[] y = new Double[TOTAL_POINTS];
        Double[] mf1 = new Double[TOTAL_POINTS];
        Double[] mf2 = new Double[TOTAL_POINTS];
        Double[] mf3 = new Double[TOTAL_POINTS];

        for (int i = 0; i < 120; i++) {
            x[i] = i / 10.0;
            mf1[i] = Math.exp((-Math.pow((x[i] - m1), 2)) / (2 * Math.pow(de1, 2)));
            mf2[i] = Math.exp((-Math.pow((x[i] - m2), 2)) / (2 * Math.pow(de2, 2)));
            mf3[i] = Math.exp((-Math.pow((x[i] - m3), 2)) / (2 * Math.pow(de3, 2)));

            double b = mf1[i] + mf2[i] + mf3[i];

            double a1 = mf1[i] * (p1 * x[i] + q1);
            double a2 = mf2[i] * (p2 * x[i] + q2);
            double a3 = mf3[i] * (p3 * x[i] + q3);

            double a = a1 + a2 + a3;
            y[i] = a / b;
        }

        mf1Curve = new Curve("mf1(x)", Arrays.asList(x), Arrays.asList(mf1));
        mf2Curve = new Curve("mf2(x)", Arrays.asList(x), Arrays.asList(mf2));
        mf3Curve = new Curve("mf3(x)", Arrays.asList(x), Arrays.asList(mf3));
        curve = new Curve("y(x)", Arrays.asList(x), Arrays.asList(y));

        calculateAptitude();
    }

    public void calculateAptitude() {
        if (referenceCurve == null) {
            referenceCurve = curve;
        }

        aptitude = 0.0;

        for (int i = 0; i < referenceCurve.getYValues().size() - 1; i++) {
            double y1 = referenceCurve.getYValues().get(i);
            // reference curves only have 12 points, but we generate curves with 120 points
            // we need to multiply the i value * 10 to get intermediate points for comparison
            double y2 = curve.getYValues().get(i * 10);

            aptitude += Math.abs(y1 - y2);
        }
    }

}