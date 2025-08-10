package de.petermeissner.schedmanager;

import de.petermeissner.schedmanager.util.JndiUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import share.schedule.ScheduleSuperInterface;
import share.schedmanager.SchedManagerInterface;

import java.util.List;


@Singleton
@Startup
public class SchedManager implements SchedManagerInterface {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    // list of all ScheduleSuper instances to be managed
    private List<ScheduleSuperInterface> scheduleSupers;

    @PostConstruct
    public void postConstruct() {
        registerScheduleSupers();
    }

    /**
     * Registers all ScheduleSuper instances found in the JNDI context.
     */
    @Override
    public void registerScheduleSupers() {
        scheduleSupers = JndiUtil.lookupScheduleSupers();
        log.info("Found {} instances of ScheduleSuper", scheduleSupers.size());
        log.info("\n    Registered ScheduleSuper instances:\n    " + String.join("    \n", listSchedules()));
    }


    /**
     * Retrieves a list of schedule names from the registered ScheduleSuper instances.
     *
     * @return a list of schedule names as strings
     */
    public List<String> listSchedules() {
        return scheduleSupers.stream()
                .map(e -> e
                        .toString()
                        .replaceAll("^.*?\"", "")
                        .replaceAll("\".*$", ""))
                .toList();
    }
}
