package share.schedmanager;

import share.schedule.ScheduleRunResult;

import java.util.HashMap;
import java.util.List;

public interface SchedManagerInterface {
    void registerScheduleSupers();

    List<String> listSchedules();

    List<HashMap<String, String>> getScheduleStatsMeasureAggregates();

    List<ScheduleRunResult> getScheduleRunResults();

    void setAllSchedulesEnabledTo(boolean enabled);
}
