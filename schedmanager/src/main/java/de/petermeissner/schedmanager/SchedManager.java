package de.petermeissner.schedmanager;

import annotations.SchedAnnotation;
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

                SchedAnnotation schedAnnotClass = obj.getClass().getAnnotation(SchedAnnotation.class);
                SchedAnnotation schedAnnotSuper = obj.getClass().getSuperclass().getAnnotation(SchedAnnotation.class);
                Annotation[] scheds = obj.getClass().getAnnotations();
                try {
                    Annotation dings = obj.getClass().getSuperclass().getAnnotations()[2];
                } catch (Exception e) {
                    // do nothing 
                }

                Annotation[] schedsSuper = obj.getClass().getAnnotations();
                Class<?> actualClass = obj.getClass();
                if (actualClass.getName().contains("Proxy")) {
                    actualClass = actualClass.getSuperclass();
                    log.info("PROXY: SchedAnnotation ? {}", obj.getClass().getSuperclass().isAnnotationPresent(SchedAnnotation.class));
                } else {
                    log.info("NOPROXY: SchedAnnotation ? {}", obj.getClass().isAnnotationPresent(SchedAnnotation.class));
                }

//                AnnotationUtil.getAnnotationNames(obj).forEach(p -> log.info("  - {}", p));

//                SchedAnnotation annot = obj.getClass().getAnnotation(SchedAnnotation.class);
//                if (null != annot) {
//                    log.info(annot.description());
//                    log.info(String.valueOf(annot.enabled()));
//                    log.info(annot.name());
//                } else {
//                    log.info("No SchedAnnotation found");
//                }
            }
        } catch (NamingException e) {
            log.error("Error listing JNDI items", e);
        }
    }
}
