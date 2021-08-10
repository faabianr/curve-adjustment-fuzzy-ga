package uag.mcc.ai.fuzzy;

import uag.mcc.ai.fuzzy.service.ChartService;
import uag.mcc.ai.fuzzy.service.GAService;

public class Application {

    public static void main(String[] args) {
        GAService gaService = new GAService(new ChartService());
        gaService.startSimulation();
    }

}
