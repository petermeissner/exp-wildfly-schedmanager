package share.schedule;

import jakarta.ejb.Remote;

import java.util.HashMap;
import java.util.List;

@Remote
public interface ScheduleSuperInterface {
    void setEnabled(boolean enabled);

    HashMap<String, String> getScheduleStatsMeasureAggregates();

    List<ScheduleRunResult> getScheduleRunResults();


}
