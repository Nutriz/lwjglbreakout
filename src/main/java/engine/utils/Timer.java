package engine.utils;

public class Timer {

    public static double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    public static float getElapsedTime(Double lastLoopTime) {
        return (float) (getTime() - lastLoopTime);
    }
}
