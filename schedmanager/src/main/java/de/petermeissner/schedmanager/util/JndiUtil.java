package de.petermeissner.schedmanager.util;

import share.schedule.ScheduleSuperInterface;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JndiUtil {
    /**
     * Lists (recursively) all JNDI items in the specified path.
     *
     * @param path             the JNDI path to query, e.g. "java:global" or "java:app"
     * @param matchNamePattern a regex pattern to match item names against; if null or empty, all items are returned
     * @param invertMatch      if true, only items that do NOT match the pattern are returned
     *
     * @return List of Hashmaps containing JNDI item details:
     *         "name": the name of the JNDI item;
     *         "className": the class name of the JNDI item;
     *         "path": the JNDI path where the item was found;
     *         "lookup": the full JNDI lookup string for the item;
     *
     * @throws NamingException if an error occurs while querying JNDI
     */
    public static List<HashMap<String, String>> listJndiItems(String path, String matchNamePattern, boolean invertMatch) throws NamingException {
        ArrayList<HashMap<String, String>> hashMapList = new ArrayList<>();

        // get context to query
        InitialContext ctx = new InitialContext();

        // list all entries in the context
        NamingEnumeration<NameClassPair> ctxListIterator = ctx.list(path);

        // go through all results
        while (ctxListIterator.hasMoreElements()) {
            // next query result
            NameClassPair pair = ctxListIterator.nextElement();

            // check if result is a JNDI context or an actual class
            // - class: store result in a HashMap
            // - context: recursively query the context
            if ("javax.naming.Context".equals(pair.getClassName())) {
                String pathx = path + "/" + pair.getName();
                hashMapList.addAll(listJndiItems(pathx, matchNamePattern, invertMatch)); // recursive call for sub-contexts
            } else {
                // check if the name matches the pattern
                if (matchNamePattern == null || matchNamePattern.isEmpty()) {
                    matchNamePattern = ".*"; // default to match all names
                }
                boolean matches = pair.getName().matches(matchNamePattern);

                // handle match invert
                if (invertMatch) {
                    matches = !matches; // invert the match result
                }

                // add results if name pattern matches
                if (matches) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("name", pair.getName());
                    hm.put("className", pair.getClassName());
                    hm.put("path", path);
                    hm.put("lookup", path + "/" + pair.getName());
                    hashMapList.add(hm);
                }
            }
        }

        return hashMapList;
    }

    /**
     * @see #listJndiItems(String, String, boolean)
     */
    public static List<HashMap<String, String>> listJndiItems(String path) throws NamingException {
        return listJndiItems(path, ".*!.*", true);
    }


    /**
     * @see #listJndiItems(String, String, boolean)
     */
    public static List<HashMap<String, String>> listJndiItems(String path, String matchNamePattern) throws NamingException {
        return listJndiItems(path, matchNamePattern, false);
    }


    /**
     * Check if an object, e.g. retrieved by JNDI lookup is a proxy object or not
     *
     * @param obj object to check
     */
    public static boolean isProxy(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return (clazz.getName().contains("Proxy") || clazz.getName().contains("$") || clazz.toString().toLowerCase().contains("proxy"));
    }

    /**
     * Looks up all instances of `ScheduleSuper` in the JNDI context.
     *
     * @return A list of `ScheduleSuper` instances found in the JNDI context.
     */
    public static List<ScheduleSuperInterface> lookupScheduleSupers() {
        List<ScheduleSuperInterface> instances = new ArrayList<>();

        // list all JNDI items in the "java:global" context
        List<HashMap<String, String>> items;
        try {
            items = listJndiItems("java:global");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // jndi context for lookup in jee environment
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // go through all JNDI items in the "java:global" context and retrieve
        for (HashMap<String, String> item : items) {
            // lookup and cast
            try {

                Object obj = ctx.lookup(item.get("lookup"));
                ScheduleSuperInterface sched = (ScheduleSuperInterface) obj;
                instances.add(sched);

            } catch (NamingException e) {

                throw new RuntimeException(e);

            } catch (ClassCastException e) {

                // If the object is not of type ScheduleSuperInterface, we do not want it - skip
                // Important: Make sure that the class itself implements ScheduleSuperInterface, it is not enough that it just inherits from a superclass that implements the interface

            }
        }

        return instances;
    }

}
