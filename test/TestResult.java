package test;

import java.util.Collections;
import java.util.List;

/**
 * Immutable holder for the aggregated experimental results of running a single
 * vertex cover approximation algorithm multiple times on the same graph instance.
 *
 * This class encapsulates the raw data collected across all execution repetitions
 * and provides some metrics for analytical evaluation:
 *
 * The average of the vertex cover sizes.
 * The average of the execution runtime measured in milliseconds.
 * The absolute minimum and maximum cover sizes achieved among all runs
 *
 */
public class TestResult {

    private final double averageCoverSize;
    private final double averageExecutionTimeMs;
    private final List<Integer> allCoverSizes;

    public TestResult(double averageCoverSize, double averageExecutionTimeMs, List<Integer> allCoverSizes) {
        this.averageCoverSize = averageCoverSize;
        this.averageExecutionTimeMs = averageExecutionTimeMs;
        this.allCoverSizes = List.copyOf(allCoverSizes);
    }

    public double getAverageCoverSize() {
        return averageCoverSize;
    }

    public double getAverageExecutionTimeMs() {
        return averageExecutionTimeMs;
    }

    public List<Integer> getAllCoverSizes() {
        return allCoverSizes;
    }

    public int getMinCoverSize() {
        if (allCoverSizes.isEmpty()) return 0;
        return Collections.min(allCoverSizes);
    }

    public int getMaxCoverSize() {
        if (allCoverSizes.isEmpty()) return 0;
        return Collections.max(allCoverSizes);
    }
}
