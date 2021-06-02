package entities;

public class CarRecording {
    private int id;
    private int cid;
    private int pid;

    public CarRecording(int id, int cid, int pid) {
        this.id = id;
        this.cid = cid;
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "ParkingQueue{" +
                "id=" + id +
                ", cid=" + cid +
                ", pid=" + pid +
                '}';
    }
}
