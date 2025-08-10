package share.schedule;

import jakarta.ejb.Remote;

@Remote
public interface ScheduleSuperInterface {
  void setEnabled(boolean enabled);
}
