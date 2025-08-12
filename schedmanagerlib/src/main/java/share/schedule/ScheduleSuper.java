package share.schedule;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.inject.Inject;
import share.schedmanager.SchedManagerInterface;

import java.util.HashMap;
import java.util.List;

/**
 * This is the base class for all schedule to be managed by SchedManager.
 * <p>
 * Both the super and subclasses must implement ScheduleSuperInterface:
 * - superclass: because it should provide most of the functionality needed so that the subclass only needs implementing
 * the actual schedule method
 * - subclass: because registration and lookup in SchedManager is done via checking for the interface
 */
public abstract class ScheduleSuper implements ScheduleSuperInterface {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public ScheduleStats scheduleStats = new ScheduleStats();

    @Inject
    Instance<SchedManagerInterface> optionalSchedManagerInstance;

    /**
     * This field is used to determine if the schedule should run or not.
     * The idea is to set it using the SchedManager, so that it can be enabled or disabled
     */
    @lombok.Getter
    @lombok.Setter
    private boolean enabled = false;

    @PostConstruct
    public void postConstruct() {
        log.info("ScheduleSuper initialized. Class: {}", this.getClass().getName());
        schedManagerTriggerRegisterScheduleSupers();
    }

    /**
     * This will trigger a refresh of the list of schedules in the SchedManager, if a schedManager is already available.
     */
    private void schedManagerTriggerRegisterScheduleSupers() {
        // This method is used to trigger the registration of schedule supers in the SchedManager
        // It is called by the SchedManager to ensure that all schedule supers are registered
        try {
            optionalSchedManagerInstance.get().registerScheduleSupers();
        } catch (UnsatisfiedResolutionException e) {
            log.info("SchedManager not available, skipping registration of {}", this.getClass().getName());
        }
    }

    public void runRun(ScheduleSuper sched) {
        handleStart();
        if (sched.shouldRun()) {
            try {
                sched.run();
                sched.handleSuccess("Schedule run completed successfully.");
            } catch (Exception e) {
                sched.handleError(e);
            }
        } else {
            sched.handleSkipped("shouldRun() == false");
        }
    }

    private void handleStart() {
        scheduleStats.logRunStart();
        log.info("Starting schedule run for {}", this.getClass().getName());
    }

    /**
     * @return true if the schedule should run, false otherwise.
     */
    public boolean shouldRun() {
        return enabled;
    }

    /**
     * This method should be implemented by subclasses to define the actual schedule logic.
     */
    public abstract void run();

    public void handleSuccess(String comment) {
        scheduleStats.logRunEndSuccess(comment);
        log.info("Schedule {} run successfully. Comment: {}", this.getClass().getName(), comment);
    }

    public void handleError(Throwable e) {
        scheduleStats.logRunEndFailure(e);
        log.error("Schedule {} run failed", this.getClass().getName(), e);

    }

    public void handleSkipped(String comment) {
        scheduleStats.logRunSkipped(comment);
        log.info("Schedule {} is not enabled, skipping run.", this.getClass().getName());
    }

    /**
     * <ul>
     * <li>
     *     This method should be implemented by subclasses.
     * </li>
     * <li>
     *     The method body should only consist of: <br>
     *      <code>runRun(this);</code>
     * </li>
     * <li>
     *      The method implementation should be annotated with <code>@Schedule(...)</code> .
     * </li>
     * </ul>
     */
    public abstract void schedule();

    /**
     * Returns a map of aggregates for the schedule statistics.
     */
    public HashMap<String, String> getScheduleStatsMeasureAggregates() {
        return scheduleStats.getMeasureAggregates(isEnabled());
    }


    /**
     * Returns a list of ScheduleRunResult objects representing the results of the schedule runs.
     */
    public List<ScheduleRunResult> getScheduleRunResults() {
        // get runs
        List<ScheduleRunResult> runs = scheduleStats.getRuns();

        // add schedule class name to each run
        runs.forEach(e -> e.setClassName(this.getClass().getSimpleName()));

        return runs;
    }
}
