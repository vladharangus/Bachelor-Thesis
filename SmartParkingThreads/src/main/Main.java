package main;

import entities.Car;
import entities.ParkingLot;
import entities.CarRecording;
import repositories.CarRepository;
import repositories.ParkingRepository;
import repositories.QueueRepository;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static CarRepository carRepository = new CarRepository();
    public static ParkingRepository parkingRepository = new ParkingRepository();
    public static QueueRepository queueRepository = new QueueRepository();
    public static Lock mutex = new ReentrantLock();
    public static Lock mutex2 = new ReentrantLock();
    public static Boolean isExit = false;


    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2-y1)* (y2-y1));
    }
    public static int getClosestParking(int cid) {
        Car c = carRepository.getById(cid);
        int pid = 0;
        double x = c.getX();
        double y = c.getY();
        double min = distance(0.0, 0.0, 1000.0, 1000.0);
        ArrayList<ParkingLot> p = parkingRepository.getParkingLots();
        for(ParkingLot pl: p) {
            double px = pl.getX();
            double py = pl.getY();
            double dist = distance(x, y, px, py);
            if(dist < min) {
                min = dist;
                pid = pl.getId();
            }
        }
        return pid;
    }

    public static ArrayList<Double> getParkingPlace() {
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

    public static ArrayList<Double> getCarPlace() {

        double x = ThreadLocalRandom.current().nextDouble(1000.0);
        double y = ThreadLocalRandom.current().nextDouble(1000.0);
        while (parkingRepository.existsPosition(x, y) && carRepository.existsPosition(x, y)) {
            x = ThreadLocalRandom.current().nextDouble(1000.0);
            y = ThreadLocalRandom.current().nextDouble(1000.0);
        }
        ArrayList<Double> res = new ArrayList<>();
        res.add(x);
        res.add(y);
        return res;
    }
    public static void generateParkingLots () throws Exception {
        for(int i = 0; i < 50; i++) {
            ArrayList<Double> pos = getParkingPlace();
            parkingRepository.save(new ParkingLot(i, pos.get(0), pos.get(1), 5));
        }
    }
    static class parkingRequestGenerator extends Thread {
        @Override
        public void run() {

            for (int i = 0; i < 1000 && !isExit; i++) {
                    try {

                        Thread.sleep(10000);
                        mutex.lock();
                        for(int k = 0; k < 5 ;k++){

                        int cid = ThreadLocalRandom.current().nextInt(carRepository.getSize());
                        while (carRepository.getById(cid) == null || queueRepository.checkCar(cid))
                            cid = ThreadLocalRandom.current().nextInt(carRepository.getSize());
                        int pid = getClosestParking(cid);
                        ParkingLot pl = parkingRepository.getById(pid);
                        if (pl.getFreespots() > 0) {
                            queueRepository.save(new CarRecording(1, cid, pid));
                            pl.setFreespots(pl.getFreespots() - 1);
                            parkingRepository.update(pl);
                        } else
                            queueRepository.save(new CarRecording(0, cid, pid));
                    }
                        mutex.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }


        }
    }

    static class deleteCarWorker extends Thread {
        @Override
        public void run() {

            for(int i = 0; i < 200 && !isExit; i++)
            {
                try {

                    Thread.sleep(40000);
                    mutex2.lock();
                    ArrayList<CarRecording> list = queueRepository.getReports();
                    for(int k = 0; k < 5; k++)
                    {
                        int cid1 = -1, cid2 = -1, pid = -1;
                        CarRecording pq1 = null;
                        for(CarRecording pq: list)
                            if (pq.getId() == 1)
                            {
                                cid1 = pq.getCid();
                                pid = pq.getPid();
                                pq1 = pq;
                                break;
                            }
                        if( cid1 != -1 && pid != -1) {
                            carRepository.delete(cid1);
                            queueRepository.delete(pq1);
                            //System.out.println("S- a sters masina " + cid1 + " din "  + pid);
                            for(CarRecording pq: list)
                                if (pq.getId() == 0 && pq.getPid() == pid)
                                {
                                    cid2 = pq.getCid();
                                    break;
                                }
                            if(cid2 != -1)  {
                                queueRepository.save(new CarRecording(1, cid2, pid));
                                //System.out.println("S-a pus masina " + cid2 + " in " + pid);
                            }
                            else {
                                ParkingLot p = parkingRepository.getById(pid);
                                p.setFreespots(p.getFreespots() + 1);
                                parkingRepository.update(p);
                            }

                        }
                    }
                    mutex2.unlock();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    static class carGenerator extends Thread {
        @Override
        public void run() {

            int id = 0;

            for(int i = 0; i < 2000 && !isExit; i++) {
                try {
                    for(int k = 0; k < 10; k++) {
                        ArrayList<Double> pos = getCarPlace();
                        boolean isE;
                        String g;
                        int nr = ThreadLocalRandom.current().nextInt(10);
                        int n2 = ThreadLocalRandom.current().nextInt(10);
                        isE = nr % 2 == 0;
                        if (n2 % 2 == 0) g = "M";
                        else g = "F";
                        carRepository.save(new Car(id, pos.get(0), pos.get(1), isE,g ));
                        id++;
                    }
                    Thread.sleep(5000);
                    //System.out.println(getThr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    static class SortbySpots implements Comparator<ParkingLot>
    {

        public int compare(ParkingLot a, ParkingLot b)
        {
            return a.getFreespots() - b.getFreespots();
        }
    }
    static class Menu {
        private final Scanner keyboard = new Scanner(System.in);
        public void runMenu() {
            while (true) {
                System.out.println("Choose one option:\n" +
                        "1. Show all the cars\n" +
                        "2. Show all the parking lots\n" +
                        "3. Show all the car recordings1\n" +
                        "4. Show the number of electrical cars in the town\n" +
                        "5. Show the Male/Female ratio in a parking lot\n" +
                        "6. Get the closest parking lot for a car\n" +
                        "7. Get the number of cars around a certain car\n" +
                        "8. Get the number of available spots in a lot\n" +
                        "9. Get the most occupied parking lots\n" +
                        "10. Exit");
                System.out.println("Enter your option");
                int option = keyboard.nextInt();
                if (option == 10) {isExit = true; break;}
                else if (option == 1) getCars();
                else if (option == 2) getParkingLots();
                else if (option == 3) getRecordings();
                else if (option == 4) getElectrical();
                else if (option == 5) getGenderRatio();
                else if (option == 6) getParking();
                else if (option == 7) getNumberOfCars();
                else if (option == 8) getAvailableSpots();
                else if (option == 9) getMostOccupied();

            }
        }

        private void getMostOccupied() {
            ArrayList<ParkingLot> list = parkingRepository.getParkingLots();
            list.sort(new SortbySpots());
            for (ParkingLot p: list)
                if(p.getFreespots() == list.get(0).getFreespots())
                    System.out.println(p.getId() + " " + p.getFreespots());

        }

        private void getAvailableSpots() {
            System.out.println("Enter the id of the parking for which you want the number of free spots: ");
            int pid = keyboard.nextInt();
            System.out.println(parkingRepository.getParkingLots().get(pid).getFreespots());

        }

        private void getNumberOfCars() {
            System.out.println("Enter the id of the car ");
            int cid = keyboard.nextInt();
            Car c = carRepository.getCars().get(cid);

            double x = c.getX();
            double y = c.getY();
            long numberOfCars = carRepository.getCars().stream().filter(car -> distance(x, y, car.getX(), car.getY()) <=1).count();
            System.out.println(numberOfCars);


        }

        private void getParking() {
            System.out.println("Enter the id of the car ");
            int cid = keyboard.nextInt();
            System.out.println("The closest parking for the car " + cid + " is: " + getClosestParking(cid));

        }

        private void getGenderRatio() {
            long startTime = System.currentTimeMillis();
            System.out.println("Enter the id of the parking for which you want the ratio: ");
            int pid = keyboard.nextInt();
            int male = 0, female = 0;
            for (CarRecording cr: queueRepository.getReports())
                if(cr.getPid() == pid && cr.getId() == 1) {
                    Car c = carRepository.getCars().get(cr.getCid());
                    if (c.getDriverGender().equals("M")) male++;
                    else female++;
                }

            System.out.println("Number of men: " + male + " Number of women: " + female);
            long endTime = System.currentTimeMillis();System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
        }

        public void getCars() {
            for (Car c: carRepository.getCars())
                System.out.println(c.getId() + " " + c.getDriverGender() + " " + c.isElectrical());
        }
        public void getParkingLots() {
            for (ParkingLot p: parkingRepository.getParkingLots())
                System.out.println(p.getId() + " " + p.getFreespots());
        }
        public void getRecordings() {
            for (CarRecording pq: queueRepository.getReports())
                System.out.println(pq.getId() + " " + pq.getCid() + " " + pq.getPid());
        }

        public void getElectrical(){
            int numberOfElectricalCars = (int) carRepository.getCars().stream().filter(Car::isElectrical).count();
            System.out.println(numberOfElectricalCars);
        }

    }
    public static void main(String[] args) throws Exception {



        generateParkingLots();

        carGenerator carGenerator = new carGenerator();
        parkingRequestGenerator parkingRequestGenerator = new parkingRequestGenerator();
        deleteCarWorker deleteCarWorker = new deleteCarWorker();

        parkingRequestGenerator.start();
        carGenerator.start();
        deleteCarWorker.start();

        Menu menu = new Menu();
        menu.runMenu();


        parkingRequestGenerator.join();
        carGenerator.join();
        deleteCarWorker.join();




    }
}