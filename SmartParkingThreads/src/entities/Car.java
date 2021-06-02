package entities;

public class Car {
    private int id;
    private double x;
    private double y;
    private boolean isElectrical;
    private String driverGender;

    public Car(int id, double x, double y, boolean isElectrical, String driverGender) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isElectrical = isElectrical;
        this.driverGender = driverGender;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isElectrical() {
        return isElectrical;
    }

    public String getDriverGender() {
        return driverGender;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", isElectrical=" + isElectrical +
                ", driverGender='" + driverGender + '\'' +
                '}';
    }
}
