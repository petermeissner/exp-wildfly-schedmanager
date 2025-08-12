package share.schedule;

import java.io.Serializable;
import java.time.Instant;

/**
 * This class represents the result of a SchedManager managed schedule run.
 */
@lombok.Getter
@lombok.Setter
public class ScheduleRunResult implements Serializable {
    private int runsCount;
    private Instant runStartTs;
    private Instant runEndTs;
    private RunResult runResult;
    private String RunResultComment;
    private String ClassName;
}
