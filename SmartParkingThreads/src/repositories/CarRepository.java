package repositories;

import entities.Car;
import entities.ParkingLot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CarRepository {
    private ArrayList<Car> cars;
    private String filename;

    public CarRepository() {
        this.cars = new ArrayList<>();
        filename = "files/Cars.txt";
    }

    public void writeToFile() throws Exception {

        try (PrintWriter b = new PrintWriter(this.filename)) {
            getCars().forEach(e -> b.println(e.getId() + " " + e.getX() + " " + e.getY() + " " +e.getDriverGender() + " " + e.isElectrical()));

        }
        catch (IOException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void save(Car c) throws Exception {
        cars.add(c);
        writeToFile();
    }
    public void delete(int id) throws Exception {
        for(Car c: cars) {
            if (c.getId() == id)
            {
                cars.remove(c);
                break;
            }
        }
        writeToFile();
    }

    public Car getById(int id) {
        for(Car c: cars) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }
    public ArrayList<Car> getCars()
    {
        return this.cars;
    }
    public boolean existsPosition(double x, double y) {
        for(Car c : getCars())
            if(c.getX() == x && c.getY() == y)
                return true;
        return false;
    }

    public int getSize() {
        return cars.size();
    }
}
