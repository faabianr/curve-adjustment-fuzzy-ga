package uag.mcc.ai.fuzzy.service_p;

import lombok.extern.slf4j.Slf4j;
import uag.mcc.ai.fuzzy.model_p.Chromosome;
import uag.mcc.ai.fuzzy.model_p.Generation;
import uag.mcc.ai.fuzzy.utils.BitUtils;
import uag.mcc.ai.fuzzy.utils.RandomizeUtils;

import java.util.*;

@Slf4j
public class GAService {

    private static final int TOTAL_CHROMOSOMES_PER_GENERATION = 100;
    private static final int TOTAL_GENERATIONS = 200;
    private static final int TOTAL_PARTICIPANTS_PER_TOURNAMENT = 5;
    private static final int MUTATION_PERCENTAGE = 10;
    private static final boolean APPLY_ELITISM = false;

    private final ChartService chartService;
    private final Chromosome referenceChromosome;
    private Generation currentGeneration;
    private int generationCount;

    public GAService(ChartService chartService) {
        this.chartService = chartService;

        referenceChromosome = new Chromosome(
                8 * Chromosome.WEIGHT,
                25 * Chromosome.WEIGHT,
                4 * Chromosome.WEIGHT,
                45 * Chromosome.WEIGHT,
                10 * Chromosome.WEIGHT,
                17 * Chromosome.WEIGHT,
                35 * Chromosome.WEIGHT
        );

        createInitialGeneration();
    }

    public void startSimulation() {

        for (int i = 0; i < TOTAL_GENERATIONS; i++) {
            generationCount = i;
            startTournamentsForCurrentGeneration();

            sleep();
            chartService.displayCharts(referenceChromosome, currentGeneration.getBestChromosome(), generationCount);
        }

        chartService.updateCurveChartWithBestOfGenerations();

    }

    private void startTournamentsForCurrentGeneration() {
        Chromosome[] newGenerationChromosomes = new Chromosome[TOTAL_CHROMOSOMES_PER_GENERATION];

        int childrenIndex = 0;

        for (int i = 0; i < TOTAL_CHROMOSOMES_PER_GENERATION / 2; i++) {
            log.debug("Picking {} participants for tournament #{}", TOTAL_PARTICIPANTS_PER_TOURNAMENT, i);

            // tournaments
            Chromosome parent1 = executeTournamentOnCurrentGeneration();
            Chromosome parent2 = executeTournamentOnCurrentGeneration();

            // reproduction
            Chromosome[] children = applyReproduction(parent1, parent2);
            newGenerationChromosomes[childrenIndex] = children[0];
            childrenIndex++;
            newGenerationChromosomes[childrenIndex] = children[1];
            childrenIndex++;
        }

        log.debug("replacing parent generation with children");

        for (Chromosome c : newGenerationChromosomes) {
            c.calculateAptitude(referenceChromosome.getCurve());
        }

        mutateChromosomes(newGenerationChromosomes);

        if (APPLY_ELITISM) {
            currentGeneration.setChromosomes(selectBestChromosomesUsingElitism(newGenerationChromosomes));
        } else {
            currentGeneration.setChromosomes(newGenerationChromosomes);
        }

        setBestOfGeneration(currentGeneration);
    }

    private Chromosome[] selectBestChromosomesUsingElitism(Chromosome[] newGenerationChromosomes) {
        List<Chromosome> allChromosomes = new ArrayList<>();
        allChromosomes.addAll(Arrays.asList(newGenerationChromosomes));
        allChromosomes.addAll(Arrays.asList(currentGeneration.getChromosomes()));

        allChromosomes.sort(Comparator.comparing(Chromosome::getAptitude));

        Chromosome[] bestChromosomes = new Chromosome[TOTAL_CHROMOSOMES_PER_GENERATION];

        for (int i = 0; i < TOTAL_CHROMOSOMES_PER_GENERATION; i++) {
            bestChromosomes[i] = allChromosomes.get(i);
        }

        return bestChromosomes;
    }

    private void mutateChromosomes(Chromosome[] chromosomes) {

        int totalAffectedElements = MUTATION_PERCENTAGE * 100 / TOTAL_CHROMOSOMES_PER_GENERATION;

        for (int i = 0; i < totalAffectedElements; i++) {

            int randomChromosomeIndex = RandomizeUtils.randomNumber(TOTAL_CHROMOSOMES_PER_GENERATION);
            int randomGenIndex = RandomizeUtils.randomNumber(Chromosome.TOTAL_GENES);
            Chromosome c = chromosomes[randomChromosomeIndex];
            int[] genes = c.getGenes();

            genes[randomGenIndex] = BitUtils.randomBitsNegation(genes[randomGenIndex], 1);
            c.setGenes(genes);
        }

    }

    private Chromosome[] applyReproduction(Chromosome parent1, Chromosome parent2) {

        // random number between 0 and 55
        int pivotNumber = RandomizeUtils.randomNumber(56);

        // get gen index
        int genIndex = pivotNumber / 8; // each gen has 8 bits

        // get offset and selected gen
        int offset = pivotNumber - (genIndex * 8);

        int selectedGenP1 = parent1.getGenByIndex(genIndex);
        int selectedGenP2 = parent2.getGenByIndex(genIndex);

        // do we need to split a number?
        if (offset > 0) {
            int[] childrenGenes = applyReproductionToGen(selectedGenP1, selectedGenP2, offset);
            selectedGenP1 = childrenGenes[0];
            selectedGenP2 = childrenGenes[1];
        }

        int[] child1Genes = new int[Chromosome.TOTAL_GENES];
        int[] child2Genes = new int[Chromosome.TOTAL_GENES];

        // interchange (reproduction)
        for (int i = 0; i < Chromosome.TOTAL_GENES; i++) {
            if (i < genIndex) {
                child1Genes[i] = parent1.getGenByIndex(i);
                child2Genes[i] = parent2.getGenByIndex(i);
            } else if (i > genIndex) {
                child1Genes[i] = parent2.getGenByIndex(i);
                child2Genes[i] = parent1.getGenByIndex(i);
            } else {
                child1Genes[i] = selectedGenP2;
                child2Genes[i] = selectedGenP1;
            }
        }

        Chromosome child1 = new Chromosome(child1Genes);
        Chromosome child2 = new Chromosome(child2Genes);

        return new Chromosome[]{child1, child2};
    }


    private int[] applyReproductionToGen(int p1Gen, int p2Gen, int offset) {
        int[] partsOfParent1 = BitUtils.binarySplit(p1Gen, offset);
        int[] partsOfParent2 = BitUtils.binarySplit(p2Gen, offset);

        int pb1 = partsOfParent1[BitUtils.PB_INDEX];
        int pa1 = partsOfParent1[BitUtils.PA_INDEX];

        int pb2 = partsOfParent2[BitUtils.PB_INDEX];
        int pa2 = partsOfParent2[BitUtils.PA_INDEX];

        int child1 = pa1 | pb2;
        int child2 = pa2 | pb1;

        int[] children = new int[2];
        children[0] = child1;
        children[1] = child2;

        return children;
    }

    private Chromosome executeTournamentOnCurrentGeneration() {
        Set<Chromosome> participants = pickRandomParticipants(currentGeneration.getChromosomes());
        return findBestParticipant(participants);
    }

    private void setBestOfGeneration(Generation generation) {
        Set<Chromosome> generationChromosomes = new HashSet<>();
        Collections.addAll(generationChromosomes, generation.getChromosomes());
        Chromosome bestOfGeneration = findBestParticipant(generationChromosomes);
        currentGeneration.setBestChromosome(bestOfGeneration);
    }

    private Chromosome findBestParticipant(Set<Chromosome> chromosomes) {
        return chromosomes.stream()
                .min(Comparator.comparing(Chromosome::getAptitude)).orElse(null);
    }

    private Set<Chromosome> pickRandomParticipants(Chromosome[] chromosomes) {
        Set<Chromosome> participants = new HashSet<>();

        while (participants.size() < TOTAL_PARTICIPANTS_PER_TOURNAMENT) {
            Chromosome participant = chromosomes[RandomizeUtils.randomNumber(TOTAL_CHROMOSOMES_PER_GENERATION)];
            participants.add(participant);
        }

        return participants;
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            log.error("error in thread sleep", e);
        }
    }

    private void createInitialGeneration() {
        Chromosome[] chromosomes = new Chromosome[TOTAL_CHROMOSOMES_PER_GENERATION];
        for (int i = 0; i < TOTAL_CHROMOSOMES_PER_GENERATION; i++) {
            // creating the chromosome with random values
            chromosomes[i] = new Chromosome(
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255),
                    RandomizeUtils.randomNumberBetweenInclusiveRange(5, 255)
            );

            // calculating aptitude value using the curve of the reference chromosome
            chromosomes[i].calculateAptitude(referenceChromosome.getCurve());

            log.debug("[generation {} - chromosome {}] aptitude={}", generationCount, i, chromosomes[i].getAptitude());
        }

        // adding the chromosomes to the generation
        currentGeneration = new Generation();
        currentGeneration.setChromosomes(chromosomes);

        log.debug("[generation {}] best chromosome: {}", generationCount, currentGeneration.getBestChromosome());
    }

}
