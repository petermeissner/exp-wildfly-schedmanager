package share.schedule;

import java.time.Instant;

/**
 * This class represents the result of a SchedManager managed schedule run.
 */
public class ScheduleRunResult {
    public int runsCount;
    public Instant runStartTs;
    public Instant runEndTs;
    public RunResult runResult;
    public String RunResultComment;
}
