package share.schedule;

import lombok.AccessLevel;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

/**
 * This class collects and handles statistics for schedules managed by SchedManager.
 */
@lombok.Getter
@lombok.Setter
public class ScheduleStats {

    @lombok.Setter(AccessLevel.NONE)
    private Instant startupTs;

    private Instant runStartTs;
    private Instant runEndTs;

    private Duration keepStatsMaxDuration;
    private int keepStatsMaxN;

    private int runsCount = 0;

    private List<ScheduleRunResult> runs = new java.util.ArrayList<>();

    private ScheduleStatsRunState runState = ScheduleStatsRunState.NEVER_STARTED;

    /**
     * Constructor to initialize ScheduleStats with specific parameters.
     *
     * @param keepStatsMaxDuration maximum duration for runs to keep stats for
     * @param keepStatsMaxN        maximum number of runs to keep stats for
     */
    public ScheduleStats(Duration keepStatsMaxDuration, int keepStatsMaxN) {
        this.startupTs = Instant.now();
        this.keepStatsMaxDuration = keepStatsMaxDuration;
        this.keepStatsMaxN = keepStatsMaxN;
        this.runs = new java.util.ArrayList<>();
    }

    /**
     * Default constructor that initializes ScheduleStats with default parameters for max duration and max N.
     */
    public ScheduleStats() {
        this.startupTs = Instant.now();
        this.keepStatsMaxDuration = Duration.ofDays(30);
        this.keepStatsMaxN = 1440;
    }

    /**
     * Log the start of a run.
     */
    public void logRunStart() {
        runStartTs = Instant.now();
        runState = ScheduleStatsRunState.RUNNING;
        runsCount++;
    }

    /**
     * @see #logRunEnd(RunResult, String)
     */
    public void logRunEndSuccess(String comment) {
        runEndTs = Instant.now();
        logRunEnd(RunResult.SUCCESS, comment);
    }

    /**
     * Log the end of a run with the specified result and comment.
     *
     * @param runResult how did the run end, e.g. SUCCESS or FAILURE
     * @param comment   addidtional information, e.g. error message in case of failure
     */
    public void logRunEnd(RunResult runResult, String comment) {
        runEndTs = Instant.now();
        runState = ScheduleStatsRunState.NOT_RUNNING;

        ScheduleRunResult srr = new ScheduleRunResult();
        srr.runsCount = runsCount;
        srr.runStartTs = runStartTs;
        srr.runEndTs = runEndTs;
        srr.runResult = runResult;
        srr.RunResultComment = comment;
        runs.add(srr);

        pruneRuns();
    }

    /**
     * Clean up runs kept according to maximum size and maximum duration.
     */
    public void pruneRuns() {
        // keep only the last N runs
        while (runs.size() > keepStatsMaxN) {
            runs.remove(0); // Remove the oldest run if we exceed the max count
        }

        // keep only runs that are within the max duration from startup
        runs.removeIf(r -> Duration.between(r.runEndTs, Instant.now()).compareTo(keepStatsMaxDuration) > 0);
    }

    /**
     * @see #logRunEnd(RunResult, String)
     */
    public void logRunEndFailure(String comment) {
        runEndTs = Instant.now();
        logRunEnd(RunResult.FAILURE, comment);
    }


    public HashMap<String, String> getMeasureAggregates() {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("N", String.valueOf(runs.size()));

        hm.put("firstStart",
                runs.stream()
                        .map(run -> run.runStartTs)
                        .min(Instant::compareTo)
                        .orElse(null).toString()
        );

        hm.put("lastStart",
                runs.stream()
                        .map(run -> run.runStartTs)
                        .max(Instant::compareTo)
                        .orElse(null).toString()
        );

        hm.put("firstEnd",
                runs.stream()
                        .map(run -> run.runEndTs)
                        .min(Instant::compareTo)
                        .orElse(null).toString()
        );

        hm.put("lastEnd",
                runs.stream()
                        .map(run -> run.runEndTs)
                        .max(Instant::compareTo)
                        .orElse(null).toString()
        );

        hm.put("averageDuration",
                String.valueOf(
                        runs.stream()
                                .mapToLong(run -> Duration.between(run.runStartTs, run.runEndTs).toSeconds())
                                .average()
                                .orElse(0)
                ));

        hm.put("successN",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isSuccess())
                                .count()
                ));

        hm.put("failureN",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isFailure())
                                .count()
                ));

        hm.put("successRate",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isSuccess())
                                .count() * 100.0 / runs.size()
                ));

        hm.put("failureRate",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isFailure())
                                .count() * 100.0 / runs.size()
                ));

        hm.put("runsPerHour",
                String.valueOf(
                        runs.stream()
                                .mapToLong(run -> Duration.between(run.runStartTs, run.runEndTs).toSeconds())
                                .sum() / 3600.0
                ));

        hm.put("runsPerDay",
                String.valueOf(
                        runs.stream()
                                .mapToLong(run -> Duration.between(run.runStartTs, run.runEndTs).toSeconds())
                                .sum() / 86400.0
                ));

        hm.put("failuresPerDay",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isFailure())
                                .mapToLong(run -> Duration.between(run.runStartTs, run.runEndTs).toSeconds())
                                .sum() / 86400.0
                ));

        hm.put("failuresPerHour",
                String.valueOf(
                        runs.stream()
                                .filter(run -> run.runResult.isFailure())
                                .mapToLong(run -> Duration.between(run.runStartTs, run.runEndTs).toSeconds())
                                .sum() / 3600.0
                ));

        return hm;
    }

    public void logRunSkipped(String comment) {
        runEndTs = Instant.now();
        runState = ScheduleStatsRunState.NOT_RUNNING;

        ScheduleRunResult srr = new ScheduleRunResult();
        srr.runsCount = runsCount;
        srr.runStartTs = runStartTs;
        srr.runEndTs = runEndTs;
        srr.runResult = RunResult.SKIPPED;
        srr.RunResultComment = comment;
        runs.add(srr);

        pruneRuns();
    }
}
