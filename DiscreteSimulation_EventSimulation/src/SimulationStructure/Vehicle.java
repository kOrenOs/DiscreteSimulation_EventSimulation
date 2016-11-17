/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class Vehicle {

    private double timeWaitingA = 0;
    private double timeWaitingB = 0;

    private double startWaiting = 0;

    private int numberOfWaitingA = 0;
    private int numberOfWaitingB = 0;

    private Random failureGenerator;

    private VehiclePerformance performance;

    private String name = "";

    private VehicleStatusEnum actualStatus;
    private Double timeOfAction;
    private double timeOfStartAction;

    public Vehicle(VehiclePerformance paPerformance, long paFailureGeneratorSeed, String paName) {
        performance = paPerformance;
        failureGenerator = new Random(paFailureGeneratorSeed);
        name = paName;
    }

    public void setStatus(VehicleStatusEnum paStatus, Double paTimeOfAction, double paTimeOfStartAction) {
        actualStatus = paStatus;
        timeOfAction = paTimeOfAction;
        if (paTimeOfAction != null) {
            timeOfStartAction = paTimeOfStartAction;
        } else {
            timeOfStartAction = 0;
        }
    }

    public VehicleStatusEnum getStatus() {
        return actualStatus;
    }

    public Double getTimeOfAction(double simTime) {
        if (timeOfAction != null) {
            return timeOfAction+timeOfStartAction - simTime;
        }
        return timeOfAction;
    }

    public void setStart(double paSimTime) {
        startWaiting = paSimTime;
    }

    public void stopTimeWaitingA(double paSimTime) {
        timeWaitingA += (paSimTime - startWaiting);
        numberOfWaitingA++;
    }

    public void stopTimeWaitingB(double paSimTime) {
        timeWaitingB += (paSimTime - startWaiting);
        numberOfWaitingB++;
    }

    public double getWaitingA() {
        return timeWaitingA;
    }

    public double getWaitingB() {
        return timeWaitingB;
    }

    public double getAvgTimeOfWaitingA() {
        return timeWaitingA / numberOfWaitingA;
    }

    public double getAvgTimeOfWaitingB() {
        return timeWaitingB / numberOfWaitingB;
    }

    public String getName() {
        return name;
    }

    public void reset(long paFailureGeneratorSeed) {
        actualStatus = VehicleStatusEnum.OutOfSimulation;
        timeOfAction = new Double(0);
        timeWaitingA = 0;
        timeWaitingB = 0;
        numberOfWaitingA = 0;
        numberOfWaitingB = 0;
        failureGenerator = new Random(paFailureGeneratorSeed);
    }

    public VehiclePerformance getPerformance() {
        return performance;
    }

    public boolean carFail() {
        if (failureGenerator.nextDouble() < performance.getFailureChance()) {
            return true;
        }
        return false;
    }
}
