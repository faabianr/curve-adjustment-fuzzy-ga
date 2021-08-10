package uag.mcc.ai.fuzzy.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Generation {

    private Chromosome[] chromosomes;
    private Chromosome bestChromosome;

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Chromosome[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    public Chromosome getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(Chromosome bestChromosome) {
        this.bestChromosome = bestChromosome;
    }

}
