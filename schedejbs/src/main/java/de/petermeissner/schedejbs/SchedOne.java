package de.petermeissner.schedejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import share.schedule.ScheduleSuper;

import java.util.Random;

@Singleton
@Startup
public class SchedOne extends ScheduleSuper implements share.schedule.ScheduleSuperInterface {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SchedOne.class);

    @PostConstruct
    public void init() {
        super.setEnabled(true);
        log.info("SchedOne initialized.");
    }



    public void run() {
        Random random = new Random();
        if (random.nextDouble() > 0.5) {
            throw new RuntimeException("Simulated failure in SchedOne payload.");
        } else {
            log.info("SchedOne payload executed successfully.");
        }
    }

    @Schedule(hour = "*", minute = "*", second = "*/20", persistent = false)
    public void schedule() {
        runRun(this);
    }
}
