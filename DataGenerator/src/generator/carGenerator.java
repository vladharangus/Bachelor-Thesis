package generator;

import entities.Car;
import repositories.CarRepository;
import repositories.ParkingRepository;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class carGenerator {
    public  CarRepository carRepository = new CarRepository();
    public ParkingRepository pRepository = new ParkingRepository();

    public carGenerator() {
    }
    public  ArrayList<Double> getCarPlace() {

        double x = ThreadLocalRandom.current().nextDouble(1000.0);
        double y = ThreadLocalRandom.current().nextDouble(1000.0);
        while (pRepository.existsPosition(x, y) && carRepository.existsPosition(x, y)) {
            x = ThreadLocalRandom.current().nextDouble(1000.0);
            y = ThreadLocalRandom.current().nextDouble(1000.0);
        }
        ArrayList<Double> res = new ArrayList<>();
        res.add(x);
        res.add(y);
        return res;
    }

    public void generateCars() throws Exception {
        int id = 0;
        for(int i = 0; i < 60000; i++) {
                ArrayList<Double> pos = getCarPlace();
                boolean isE;
                String g;
                int nr = ThreadLocalRandom.current().nextInt(10);
                int n2 = ThreadLocalRandom.current().nextInt(10);
                isE = nr % 2 == 0;
                if (n2 % 2 == 0) g = "M";
                else g = "F";
                carRepository.save(new Car(id, pos.get(0), pos.get(1), isE, g));
                id++;
        }

    }
}
