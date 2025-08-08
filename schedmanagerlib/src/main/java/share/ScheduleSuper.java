package share;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.inject.Inject;

public class ScheduleSuper {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    public boolean enebled = true;


    @Inject
    Instance<SchedManagerInterface> optionalSchedManagerInstance;


    @PostConstruct
    public void postConstruct() {
        log.info("ScheduleSuper initialized. Class: {}", this.getClass().getName());
        schedManagerTriggerRegisterScheduleSupers();
    }

    /**
     * This will trigger a refresh of the list of schedules in the SchedManager
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
}
