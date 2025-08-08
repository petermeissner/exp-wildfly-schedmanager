package share;

import jakarta.ejb.Remote;

@Remote
public interface ScheduleSuperInterface {
    void run();
}
