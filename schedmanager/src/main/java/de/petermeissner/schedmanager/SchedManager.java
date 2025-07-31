package de.petermeissner.schedmanager;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class SchedManager {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void postConstruct() {
        log.info("SchedManager initialized. Querying EJBs...");
        listJndi("").forEach(name -> log.info("Found EJB: {}", name));
        listJndi("java:global").forEach(name -> log.info("Found EJB: {}", name));
    }

    public List<String> listJndi(String path) {
        ArrayList<String> lst = new ArrayList<>();

        try {
            InitialContext ctx = new InitialContext();
            NamingEnumeration<NameClassPair> list = ctx.list(path);
            log.info("List of EJBs in the application server:");
            while (list.hasMoreElements()) {
                NameClassPair pair = list.nextElement();
                String name = pair.getName();
                lst.add(name);
            }
        } catch (Exception e) {
            log.error("Error querying JNDI for EJBs", e);
        }

        return lst;
    }
}
