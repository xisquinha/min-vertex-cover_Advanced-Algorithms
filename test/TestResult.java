package test;

/**
 * Immutable holder for the aggregated results of running one algorithm
 * several times on the same graph instance.
 *
 * Both values are averages over all repetitions performed by
 * TestResult
 */
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
