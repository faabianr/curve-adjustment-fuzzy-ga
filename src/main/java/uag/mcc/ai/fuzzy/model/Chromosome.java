package uag.mcc.ai.fuzzy.model;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Chromosome {

    public static final int WEIGHT = 5;
    public static final int TOTAL_CURVE_POINTS = 1000;

    public static final int TOTAL_GENES = 7;

    private static final int A = 0;
    private static final int B = 1;
    private static final int C = 2;
    private static final int D = 3;
    private static final int E = 4;
    private static final int F = 5;
    private static final int G = 6;

    private int[] genes;

    @ToString.Exclude
    private Curve curve;
    private double aptitude;

    public Chromosome(int[] genes) {
        this.setGenes(genes);
    }

    public Chromosome(int a, int b, int c, int d, int e, int f, int g) {
        genes = new int[TOTAL_GENES];

        this.setA(a);
        this.setB(b);
        this.setC(c);
        this.setD(d);
        this.setE(e);
        this.setF(f);
        this.setG(g);

        generateCurvePoints();
    }

    public void setA(int a) {
        this.genes[A] = a;
    }

    public void setB(int b) {
        this.genes[B] = b;
    }

    public void setC(int c) {
        this.genes[C] = c;
    }

    public void setD(int d) {
        this.genes[D] = d;
    }

    public void setE(int e) {
        this.genes[E] = e;
    }

    public void setF(int f) {
        this.genes[F] = f;
    }

    public void setG(int g) {
        this.genes[G] = g;
    }

    public int getA() {
        return this.genes[A];
    }

    public int getB() {
        return this.genes[B];
    }

    public int getC() {
        return this.genes[C];
    }

    public int getD() {
        return this.genes[D];
    }

    public int getE() {
        return this.genes[E];
    }

    public int getF() {
        return this.genes[F];
    }

    public int getG() {
        return this.genes[G];
    }

    public Curve getCurve() {
        return curve;
    }

    public int getGenByIndex(int index) {
        return genes[index];
    }

    public double getAptitude() {
        return aptitude;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;

        generateCurvePoints();
    }

    public int[] getGenes() {
        return genes;
    }

    public void calculateAptitude(Curve referenceCurve) {
        for (int i = 0; i < TOTAL_CURVE_POINTS; i++) {
            double y1 = referenceCurve.getYValues().get(i);
            double y2 = curve.getYValues().get(i);

            aptitude += Math.abs(y1 - y2);
        }
    }

    public void generateCurvePoints() {
        curve = new Curve();
        for (int i = 0; i < TOTAL_CURVE_POINTS; i++) {
            curve.addPoint(calculateX(i), calculateY(i));
        }
    }

    private double calculateX(int i) {
        return i / 10.0;
    }

    private int applyWeight(int n) {
        return n / WEIGHT;
    }

    private double calculateY(int i) {
        double Xi = calculateX(i);
        int A = applyWeight(getA());
        int B = applyWeight(getB());
        int C = applyWeight(getC());
        int D = applyWeight(getD());
        int E = applyWeight(getE());
        int F = applyWeight(getF());
        int G = applyWeight(getG());

        return A * (B * Math.sin(Xi / C) + D * Math.cos(Xi / E)) + F * Xi - G;
    }

}
