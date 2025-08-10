package share.schedule;

public enum RunResult {
    SUCCESS,
    SKIPPED,
    FAILURE;


    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public boolean isFailure() {
        return this == FAILURE;
    }


}
