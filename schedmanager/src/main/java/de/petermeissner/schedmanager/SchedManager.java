package de.petermeissner.schedmanager;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import util.JndiUtil;

import javax.naming.InitialContext;
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

            InitialContext ctx = new InitialContext();

            Object obj = ctx.lookup("java:global/schedmanager-1.0-SNAPSHOT/SchedManager!de.petermeissner.schedmanager.SchedManager");
            log.info("Lookup result: {}", obj);

            Object obj2 = ctx.lookup("java:global/schedmanager-1.0-SNAPSHOT/SchedManager");
            log.info("Lookup result: {}", obj2);

            for ( HashMap<String, String> item : objList) {
//                String lookup = item.get("path") + "/" + item.get("name");
//                log.info("path: {}; name: {}; className {}", item.get("path"), item.get("name"), item.get("className"));
                log.info("Looking up : {}", item.get("lookup"));
                log.info("object lookup:" + ctx.lookup(item.get("lookup")));
            }
        } catch (NamingException e) {
            log.error("Error listing JNDI items", e);
        }
    }
}
