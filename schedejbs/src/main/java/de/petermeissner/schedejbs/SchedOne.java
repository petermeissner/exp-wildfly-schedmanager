package de.petermeissner.schedejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import share.ScheduleSuper;

@Singleton
@Startup
public class SchedOne extends ScheduleSuper implements share.ScheduleSuperInterface {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SchedOne.class);

    @PostConstruct
    public void init() {
        log.info("SchedOne initialized.");
    }

    @Override
    public void run() {

    }
}
