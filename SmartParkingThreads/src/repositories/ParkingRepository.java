package repositories;

import entities.Car;
import entities.ParkingLot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ParkingRepository {
    private final ArrayList<ParkingLot>  parkingLots;
    private final String filename;

    public ParkingRepository() {
        this.parkingLots = new ArrayList<>();
        filename = "files/Lots.txt";
    }

    public void writeToFile() throws Exception {

        try (PrintWriter b = new PrintWriter(this.filename)) {
            getParkingLots().forEach(e -> b.println(e.getId()+ " "+ e.getX() + " "+e.getY() + " "+e.getSpots() + " " +e.getFreespots()));
        }
        catch (IOException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void save(ParkingLot p) throws Exception {
        parkingLots.add(p);
        writeToFile();
    }

    public ParkingLot getById(int id) {
        for(ParkingLot p : parkingLots) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }
    public ArrayList<ParkingLot> getParkingLots()
    {
        return this.parkingLots;
    }
    public boolean existsPosition(double x, double y) {
        for(ParkingLot p : parkingLots)
            if(p.getX() == x && p.getY() == y)
                return true;
         return false;
    }

    public void update(ParkingLot pl) throws Exception {
        parkingLots.set(pl.getId(), pl);
        writeToFile();
    }
}
