package uag.mcc.ai.fuzzy;

import uag.mcc.ai.fuzzy.model.Chromosome;
import uag.mcc.ai.fuzzy.service.CurveSynthesis;
import uag.mcc.ai.fuzzy.service_p.ChartService;
import uag.mcc.ai.fuzzy.service_p.GAService;

public class Application {

    public static void main(String[] args) {
        // runCurveAdjustmentWithSimpleGA();
        runCurveAdjustmentUsingFuzzyAndGA();
    }

    public static void runCurveAdjustmentUsingFuzzyAndGA() {
        Chromosome tempChromosome = Chromosome.builder()
                .m1(0.8)
                .m2(6.4)
                .m3(12.2)
                .de1(3.5)
                .de2(1.1)
                .de3(4.3)
                .p1(2.7)
                .p2(0.1)
                .p3(-0.3)
                .q1(23.5)
                .q2(24.5)
                .q3(29.8)
                .build();

        Chromosome rainChromosome = Chromosome.builder()
                .m1(1.2)
                .m2(3.7)
                .m3(12.1)
                .de1(2.5)
                .de2(2)
                .de3(2.3)
                .p1(1)
                .p2(4.8)
                .p3(1.1)
                .q1(22)
                .q2(9.7)
                .q3(9.3)
                .build();

        CurveSynthesis curveSynthesis = CurveSynthesis.builder()
                .tempChromsome(tempChromosome)
                .rainChromosome(rainChromosome)
                .build();

        curveSynthesis.execute();
    }

    public static void runCurveAdjustmentWithSimpleGA() {
        GAService gaService = new GAService(new ChartService());
        gaService.startSimulation();
    }

}
