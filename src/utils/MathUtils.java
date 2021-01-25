package utils;

public class MathUtils {

    public static double random(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static double clamp(double x, double min, double max) {
        if (x < min) {
            return min;
        } else if (x > max) {
            return max;
        }
        return x;
    }

    public static int clamp(int x, int min, int max) {
        if (x < min) {
            return min;
        } else if (x > max) {
            return max;
        }
        return x;
    }
}
