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

# Next Steps

- lookup of all EJBs that are instances of a specific class
- try casting to interface instead of class

