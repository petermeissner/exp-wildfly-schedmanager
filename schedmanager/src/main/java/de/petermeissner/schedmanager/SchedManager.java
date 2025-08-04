package de.petermeissner.schedmanager;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import util.JndiUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.annotation.Annotation;
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

            for (HashMap<String, String> item : objList) {
                log.info("Looking up : {}", item.get("lookup"));

                Object obj = ctx.lookup(item.get("lookup"));
                log.info("object lookup:" + obj);

                Annotation[] scheds = obj.getClass().getAnnotations();
                try {
                    Annotation dings = obj.getClass().getSuperclass().getAnnotations()[2];
                } catch (Exception e) {
                    // do nothing 
                }

                Annotation[] schedsSuper = obj.getClass().getAnnotations();
                Class<?> actualClass = obj.getClass();


            }
        } catch (NamingException e) {
            log.error("Error listing JNDI items", e);
        }
    }
}
