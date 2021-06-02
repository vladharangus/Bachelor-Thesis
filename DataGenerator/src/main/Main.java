package main;


import generator.carGenerator;
import generator.parkingGenerator;

public class Main {

    public static void main(String[] args) throws Exception {
        carGenerator cg = new carGenerator();
        parkingGenerator pg = new parkingGenerator();

        pg.generateParkingLots();
        cg.generateCars();


    }
}
