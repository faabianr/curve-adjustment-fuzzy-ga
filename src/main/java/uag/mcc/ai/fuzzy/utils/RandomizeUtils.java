package uag.mcc.ai.fuzzy.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomizeUtils {

    private RandomizeUtils() {
    }

    /**
     * Returns a random number between a range. The min value is inclusive and the max one is exclusive.
     *
     * @param min the min number of the range. This number is inclusive.
     * @param max the max number of the range. This number is exclusive.
     * @return a random int generated using the given min and max values.
     */
    public static int randomNumberBetweenRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findAny().getAsInt();
    }

    /**
     * Returns a random number between a range. The min and max values are inclusive.
     *
     * @param min the min number of the range. This number is inclusive.
     * @param max the max number of the range. This number is inclusive.
     * @return a random int generated using the given min and max values.
     */
    public static int randomNumberBetweenInclusiveRange(int min, int max) {
        return randomNumberBetweenRange(min, max + 1);
    }

    public static int randomNumberZeroOrOne() {
        Random random = new Random();
        return (random.nextInt(2) + 1) - 1;
    }

    public static int randomNumber(int maxNumber) {
        return (int) (Math.random() * maxNumber);
    }

}
