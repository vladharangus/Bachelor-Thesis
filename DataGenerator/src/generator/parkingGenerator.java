package generator;

import entities.ParkingLot;
import repositories.ParkingRepository;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class parkingGenerator {
    public ParkingRepository parkingRepository;

    public parkingGenerator() {
        this.parkingRepository = new ParkingRepository();
    }
    private ArrayList<Double> getParkingPlace() {
        double x = ThreadLocalRandom.current().nextDouble(1000.0);
        double y = ThreadLocalRandom.current().nextDouble(1000.0);
        while (parkingRepository.existsPosition(x, y)) {
            x = ThreadLocalRandom.current().nextDouble(1000.0);
            y = ThreadLocalRandom.current().nextDouble(1000.0);
        }
        ArrayList<Double> res = new ArrayList<>();
        res.add(x);
        res.add(y);
        return res;
    }
    public void generateParkingLots () throws Exception {
        for(int i = 0; i < 50; i++) {
            ArrayList<Double> pos = getParkingPlace();
            parkingRepository.save(new ParkingLot(i, pos.get(0), pos.get(1), 5));
        }
    }
}
