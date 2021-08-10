package uag.mcc.ai.fuzzy.utils;

public class BitUtils {

    public static final int PB_INDEX = 0;
    public static final int PA_INDEX = 1;

    private BitUtils() {
    }

    public static int[] binarySplit(int n, int offset) {
        String binaryString = String.format("%8s", Integer.toBinaryString(n)).replaceAll(" ", "0");

        StringBuilder pa = new StringBuilder();
        StringBuilder pb = new StringBuilder();

        pb.append("0000");

        for (int i = 0; i < binaryString.length(); i++) {
            if (i < offset) {
                pa.append(binaryString.charAt(i));
            } else {
                pb.append(binaryString.charAt(i));
            }
        }

        pa.append("0000");

        int[] parts = new int[2];

        parts[PB_INDEX] = Integer.parseInt(pb.toString(), 2);
        parts[PA_INDEX] = Integer.parseInt(pa.toString(), 2);

        return parts;
    }

    public static int randomBitsNegation(int n, int totalNegated) {
        String binaryString = String.format("%8s", Integer.toBinaryString(n)).replaceAll(" ", "0");

        for (int i = 0; i < totalNegated; i++) {
            binaryString = randomBitNegation(binaryString);
        }

        return Integer.parseInt(binaryString, 2);
    }

    private static String randomBitNegation(String binaryString) {
        int[] numbers = new int[8];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(String.valueOf(binaryString.charAt(i)));
        }

        int randomIndex = RandomizeUtils.randomNumber(7);

        if (numbers[randomIndex] == 0) {
            numbers[randomIndex] = 1;
        } else {
            numbers[randomIndex] = 0;
        }

        StringBuilder sb = new StringBuilder();

        for (int number : numbers) {
            sb.append(number);
        }

        return sb.toString();
    }

}
