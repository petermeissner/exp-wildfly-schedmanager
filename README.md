# Description

This is a playground for testing possible approaches to handle schedules in a more controlled and custom way.

- get a list of all schedules
- allow to handle schedules in bulk 
- get statistics from schedules: status, runtime
  

# Learnings

## Get a List of all 'Schedules'

This can be done using JNDI. It is a little bit tricky since the same resource might be listed multiple times with different paths but other than that it works and instances can be looked up.

## Using Annotations on EJBs

Using Annotations on EJBs sucks. 

The problem is, that interaction with EJBs is - per design - not direct but via proxy. This means it is easy to get a list of running EJBs via JNDI and loop over them, but it is not really possible to get and check for specific annotations on those EJBs because the EJBs themselves are proxied and, it seems, the annotations are proxied as well.  

See commit:  56b0d70
