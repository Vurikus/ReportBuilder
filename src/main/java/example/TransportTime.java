package example;

public class TransportTime {
    private int run;
    private int idle;
    private int repair;

    public TransportTime(int run, int idle, int repair) {
        this.run = run;
        this.idle = idle;
        this.repair = repair;
    }

    public int getRun() {
        return run;
    }

    public int getIdle() {
        return idle;
    }

    public int getRepair() {
        return repair;
    }
}
