package test;

public class TestResult {

    private final double averageCoverSize;
    private final double averageExecutionTimeMs;

    public TestResult(double averageCoverSize, double averageExecutionTimeMs) {
        this.averageCoverSize = averageCoverSize;
        this.averageExecutionTimeMs = averageExecutionTimeMs;
    }

    public double getAverageCoverSize() {
        return averageCoverSize;
    }

    public double getAverageExecutionTimeMs() {
        return averageExecutionTimeMs;
    }
}
