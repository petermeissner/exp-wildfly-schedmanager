package de.petermeissner.schedmanager;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import share.schedmanager.SchedManagerInterface;
import share.schedule.ScheduleRunResult;

import java.util.HashMap;
import java.util.List;

@Path("/")
public class SchedManagerRest {

    @Inject
    SchedManagerInterface schedManagerInterface;

    @GET
    @Produces("text/html")
    public String root() {
        return "<html><body>" +
                "<h1>SchedManager REST API</h1>" +
                "<p>Available endpoints:</p>" +
                "<ul>" +
                "<li><a href=\"./schedmanager/list_schedules\">List Schedules</a></li>" +
                "<li><a href=\"./schedmanager/list_schedule_stat_aggregates\">List Schedule Stats Aggregates</a></li>" +
                "<li><a href=\"./schedmanager/list_schedule_run_results\">List Schedule Run Results</a></li>" +
                "<li><a href=\"./schedmanager/enable_all_schedules\">Enable All Schedules</a></li>" +
                "<li><a href=\"./schedmanager/disable_all_schedules\">Disable All Schedules</a></li>" +
                "</ul>" +
                "<p>Use the above links to access the SchedManager REST API.</p>"
                ;
    }

    @GET
    @Path("list_schedules")
    @Produces("application/json")
    public List<String> listSchedules() {
        return schedManagerInterface.listSchedules();
    }

    @GET
    @Path("list_schedule_stat_aggregates")
    @Produces("application/json")
    public List<HashMap<String, String>> listScheduleStats() {
        return schedManagerInterface.getScheduleStatsMeasureAggregates();
    }


    @GET
    @Path("list_schedule_run_results")
    @Produces("application/json")
    public List<ScheduleRunResult> listScheduleRunResults() {
        List<ScheduleRunResult> smResults = schedManagerInterface.getScheduleRunResults();
        return smResults;
    }

    @GET
    @Path("enable_all_schedules")
    @Produces("application/json")
    public String enableAllSchedules() {
        schedManagerInterface.setAllSchedulesEnabledTo(true);
        return "done";
    }

    @GET
    @Path("disable_all_schedules")
    @Produces("application/json")
    public String disableAllSchedules() {
        schedManagerInterface.setAllSchedulesEnabledTo(false);
        return "done";
    }

}
