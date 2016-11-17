/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import java.util.LinkedList;

/**
 *
 * @author korenciak.marek
 */
public class RowOfVehicles {

    private LinkedList<Vehicle> row = new LinkedList<>();
    private double lastChangeTime = 0;
    private double totalCount = 0;

    public Vehicle getNextVehicle() {
        return row.get(0);
    }

    public Vehicle removeFirstVehicle(double paSimTime) {
        addToStatistics(paSimTime);
        return row.remove(0);
    }

    public void addVehicle(Vehicle paVehicle, double paSimTime) {
        addToStatistics(paSimTime);
        paVehicle.setStart(paSimTime);
        row.add(paVehicle);
    }

    public void reset() {
        row.clear();
        totalCount = 0;
        lastChangeTime = 0;
    }

    public boolean isEmpty() {
        return row.isEmpty();
    }

    private void addToStatistics(double paSimTime) {
        totalCount += (paSimTime - lastChangeTime) * row.size();
        lastChangeTime = paSimTime;
    }

    public double getStatisticsData(double paSimTime) {
        addToStatistics(paSimTime);
        return totalCount;
    }
}
