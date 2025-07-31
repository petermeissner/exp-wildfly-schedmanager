package de.petermeissner.schedejbs;

import annotations.SchedAnnotation;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
@SchedAnnotation(name = "SchedOne", description = "First scheduled EJB", enabled = true)
public class SchedOne {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SchedOne.class);

    @PostConstruct
    public void init() {
        log.info("SchedOne initialized.");
    }
}
