package entities;

public class ParkingLot {
    private int id;
    private double x;
    private double y;
    private int spots;
    private int freespots;

    public ParkingLot(int id, double x, double y, int spots) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.spots = spots;
        this.freespots = spots;
    }

    public int getId() {
        return id;
    }

    public int getSpots() {
        return spots;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getFreespots() {
        return freespots;
    }

    public void setFreespots(int freespots) {
        this.freespots = freespots;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", spots=" + spots +
                ", freespots=" + freespots +
                '}';
    }
}
