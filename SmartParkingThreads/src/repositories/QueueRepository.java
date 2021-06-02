package repositories;

import entities.CarRecording;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class QueueRepository {
    private ArrayList<CarRecording> parkingQueues;
    private String filename;

    public QueueRepository() {
        this.parkingQueues = new ArrayList<>();
        filename = "files/Reports.txt";
    }

    public void writeToFile() throws Exception {

        try (PrintWriter b = new PrintWriter(this.filename)) {
            getReports().forEach(e -> b.println(e.getId() + " " + e.getCid() + " " + e.getPid()));
        }
        catch (IOException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public void save(CarRecording pq) throws Exception {
        parkingQueues.add(pq);
        writeToFile();
    }
    public void delete(CarRecording pq) throws Exception {
        parkingQueues.remove(pq);
        writeToFile();
    }

    public ArrayList<CarRecording> getReports()
    {
        return this.parkingQueues;
    }

    public boolean checkCar(int cid) {
        for(CarRecording pq: parkingQueues) {
            if (pq.getCid() == cid)
                return true;
        }
        return false;
    }
}
