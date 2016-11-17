/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;

/**
 *
 * @author korenciak.marek
 */
public class Statistics {

    private LinkedList<Vehicle> vehicles;

    private RowOfVehicles rowA;
    private RowOfVehicles rowB;

    private double vehiclesAvgSumWaitingRowATotal = 0;
    private double vehiclesAvgSumWaitingRowBTotal = 0;

    private double vehiclesAvgWaitingRowATotal = 0;
    private double vehiclesAvgWaitingRowBTotal = 0;

    private double AvgRowASizeTotal = 0;
    private double AvgRowBSizeTotal = 0;

    private double simTimeTotal = 0;
    private double simTimeTotalPower = 0;

    private double sumMoveVehicles = 0;

    private double moveVehicle = 0;
    private double lastChange = 0;
    private int countOfVehicles;

    private int replicationCount = 0;

    private ArrayList<Double> simulationTime;

    public Statistics() {
        vehicles = new LinkedList<>();
        simulationTime = new ArrayList<>();
    }

    public void setVehicleCount(int paVehicleCount) {
        countOfVehicles = paVehicleCount;
        moveVehicle = 0;
        lastChange = 0;
    }

    public void addVehicle(Vehicle paVehicle) {
        vehicles.add(paVehicle);
    }

    public void setRowA(RowOfVehicles paRow) {
        rowA = paRow;
    }

    public void setRowB(RowOfVehicles paRow) {
        rowB = paRow;
    }

    public void carStay(double time) {
        moveVehicle += countOfVehicles * (time - lastChange);
        countOfVehicles--;
        lastChange = time;
    }

    public void carMove(double time) {
        moveVehicle += countOfVehicles * (time - lastChange);
        countOfVehicles++;
        lastChange = time;
    }

    public double getMove(double time) {
        moveVehicle += countOfVehicles * (time - lastChange);
        lastChange = time;
        return moveVehicle;
    }

    private double getStatisticsAvgRowASize(double paSimTime) {
        return rowA.getStatisticsData(paSimTime) / paSimTime;
    }

    private double getStatisticsAvgRowBSize(double paSimTime) {
        return rowB.getStatisticsData(paSimTime) / paSimTime;
    }

    public synchronized void replicationEnd(double paSimTime) {
        for (Vehicle vehicle : vehicles) {
            vehiclesAvgSumWaitingRowATotal += vehicle.getWaitingA();
            vehiclesAvgSumWaitingRowBTotal += vehicle.getWaitingB();

            vehiclesAvgWaitingRowATotal += vehicle.getAvgTimeOfWaitingA();
            vehiclesAvgWaitingRowBTotal += vehicle.getAvgTimeOfWaitingB();
        }

        AvgRowASizeTotal += getStatisticsAvgRowASize(paSimTime);
        AvgRowBSizeTotal += getStatisticsAvgRowBSize(paSimTime);

        simTimeTotal += paSimTime;
        simTimeTotalPower += Math.pow(paSimTime, 2);

        sumMoveVehicles += getMove(paSimTime) / paSimTime;

        replicationCount++;

        simulationTime.add(simTimeTotal / replicationCount);

    }

    public double getStatisticsAvgSumWaitingRowATotal() {
        return vehiclesAvgSumWaitingRowATotal / replicationCount;
    }

    public double getStatisticsAvgSumWaitingRowBTotal() {
        return vehiclesAvgSumWaitingRowBTotal / replicationCount;
    }

    public double getStatisticsAvgTimeOfWaitingRowATotal() {
        return (vehiclesAvgWaitingRowATotal / replicationCount) / vehicles.size();
    }

    public double getStatisticsAvgTimeOfWaitingRowBTotal() {
        return (vehiclesAvgWaitingRowBTotal / replicationCount) / vehicles.size();
    }

    public double getStatisticsAvgRowASizeTotal() {
        return AvgRowASizeTotal / replicationCount;
    }

    public double getStatisticsAvgRowBSizeTotal() {
        return AvgRowBSizeTotal / replicationCount;
    }

    public double getSimTime() {
        return simTimeTotal / replicationCount;
    }

    public double getMoveVehicle() {
        return sumMoveVehicles / replicationCount;
    }

    public synchronized ArrayList<Double> getDataSet() {
        return simulationTime;
    }

    public int getReplicationNumber() {
        return replicationCount;
    }

    public double[] getSimTimeIS(double confidenceLevel) {
        double realLevel = 1 - ((1 - confidenceLevel) / 2);

        double[] confidenceInterval = new double[2];
        double avg = simTimeTotal / replicationCount;
        double S = Math.sqrt((simTimeTotalPower / replicationCount) - Math.pow(avg, 2));

        double distrib = 0;

        if (replicationCount > 1) {
            if (replicationCount < 30) {
                TDistribution student = new TDistribution(replicationCount);
                distrib = student.inverseCumulativeProbability(realLevel);
            } else {
                NormalDistribution normal = new NormalDistribution(0, 1);
                distrib = normal.inverseCumulativeProbability(realLevel);
            }

            confidenceInterval[0] = avg - (distrib * S / Math.sqrt((replicationCount - 1)));
            confidenceInterval[1] = avg + (distrib * S / Math.sqrt((replicationCount - 1)));
        } else {
            return new double[]{0, 0};
        }

        return confidenceInterval;
    }

    public void reset() {
        vehicles.clear();
    }
}
