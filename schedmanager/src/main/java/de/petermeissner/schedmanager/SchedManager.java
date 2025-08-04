package de.petermeissner.schedmanager;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import share.ScheduleSuper;
import util.JndiUtil;

import javax.naming.NamingException;
import java.util.HashMap;
import java.util.List;


@Singleton
@Startup
public class SchedManager {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());


    @PostConstruct
    public void postConstruct() {
        log.info("SchedManager initialized. Querying EJBs...");

        try {
            List<HashMap<String, String>> objList = JndiUtil.listJndiItems("java:global");
            objList.forEach(p -> log.info("Found in \"java:global\": \n  {} \n  {} \n  {}", p.get("path"), p.get("name"), p.get("lookup")));
        } catch (NamingException e) {
            log.error("Error during JNDI lookup", e);
        }

        List<ScheduleSuper> instances = JndiUtil.lookupInstancesOfScheduleSuper();
        log.info("Found {} instances of ScheduleSuper", instances.size());
    }
}
