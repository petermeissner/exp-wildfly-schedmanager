# Description

This is a playground for testing possible approaches to handle schedules in a more controlled and custom way.

- get a list of all schedules
- allow to handle schedules in bulk 
- get statistics from schedules: status, runtime
  


# Repo Structure

## schedejbs

- example implementation of a schedmanager managed schedule


## schedmanager

- the schedmanager itself
- schedmanager is an EJB for deploying in Jakarta EE environments - e.g. Wildfly

## schedmanagerlib

- utils for schedmanager
- interfaces and things alike that need to be imported by schedmanager and ejbs that need to managed by schedmanager 
                             
# Learnings

## Get a List of all 'Schedules'

This can be done using JNDI. It is a little bit tricky since the same resource might be listed multiple times with different paths but other than that it works and instances can be looked up.

## Using Annotations on EJBs

Using Annotations on EJBs sucks. 

The problem is, that interaction with EJBs is - per design - not direct but via proxy. This means it is easy to get a list of running EJBs via JNDI and loop over them, but it is not really possible to get and check for specific annotations on those EJBs because the EJBs themselves are proxied and, it seems, the annotations are proxied as well.  

See commit:  56b0d70
                             
## Using Inheritance on EJBs

- checking EJBs for inheritance is hard because they are proxied and things like `instanceof` do not work as expected
- listing and looking up EJBs works but there is a problem with using them because they cannot be cast to a type because they are proxied


## Intercepting or Surrounding Method Call

It would be very beneficial to encapsulate/surround/intercept the run method call in all schedules to ensure different things:
- catch errors 
- log start and end of the run
- handle errors

This does not seem to be an easy thing to implement since it is not easy in Java to intercept method calls. There seem to be some approaches that involve proxying, Aspect Oriented Programming, or CDI interceptors but this needs more investigation and propably also dome discussion.

So, for now I skip solving this in the most perfect and elegant way but providing instead a simple guide on how to implent a schedule that makes use of the framework: 

```java
@Singleton
@Startup
public class SchedExample extends ScheduleSuper implements share.schedule.ScheduleSuperInterface {

    public void run() {
        // payload of the schedule, called by ScheduleSuper.runRun(this)
    }

    /**
     * Schdule definition
     */
    @Schedule(hour = "*", minute = "*", second = "*/20", persistent = false)
    public void schedule() {
        runRun(this);
    }
}
```
