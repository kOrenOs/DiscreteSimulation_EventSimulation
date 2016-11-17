/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import Constants.SimWorkConstants;
import Graphics.ISimGraphics;
import Random.SeedGenerator;
import SimulationEvents.StartServiceAEvent;
import SimulationEvents.StartServiceBEvent;
import SimulationEvents.StartSimulationEvent;
import SimulationEvents.StartWaitingRowAEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import simCoreAbstraction.AbstrEvent;

/**
 *
 * @author korenciak.marek
 */
public class SimCore extends simCoreAbstraction.AbstrSimCore {

    private SeedGenerator seedRenerator;

    private LinkedList<ISimGraphics> GUIOutputs;

    private LinkedList<Vehicle> vehicles;

    private RowOfVehicles rowOfVehicleA;
    private RowOfVehicles rowOfVehicleB;

    private NonJumpInLineRoad roadAB;
    private NonJumpInLineRoad roadCA;

    private VehiclePerformance A1;
    private VehiclePerformance A2;
    private VehiclePerformance A3;
    private VehiclePerformance A4;
    private VehiclePerformance A5;

    private boolean machineAWorking = false;
    private double machineAWorkStartTime = 0;
    private double actualMaxAProgress = 0;
    private Vehicle rowA;

    private boolean machineBWorking = false;
    private double machineBWorkStartTime = 0;
    private double actualMaxBProgress = 0;
    private Vehicle rowB;

    private Statistics statisticsInstance;

    private double transported = 0;
    private double taken = 0;

    public SimCore() {
        super();
        GUIOutputs = new LinkedList<>();
    }

    @Override
    protected void preSimulationRun() {
        seedRenerator = new SeedGenerator();

        A1 = new VehiclePerformance(SimWorkConstants.vehicleA1Capacity, SimWorkConstants.vehicleA1Speed,
                SimWorkConstants.vehicleA1FailureChance, SimWorkConstants.vehicleA1RepairTime);
        A2 = new VehiclePerformance(SimWorkConstants.vehicleA2Capacity, SimWorkConstants.vehicleA2Speed,
                SimWorkConstants.vehicleA2FailureChance, SimWorkConstants.vehicleA2RepairTime);
        A3 = new VehiclePerformance(SimWorkConstants.vehicleA3Capacity, SimWorkConstants.vehicleA3Speed,
                SimWorkConstants.vehicleA3FailureChance, SimWorkConstants.vehicleA3RepairTime);
        A4 = new VehiclePerformance(SimWorkConstants.vehicleA4Capacity, SimWorkConstants.vehicleA4Speed,
                SimWorkConstants.vehicleA4FailureChance, SimWorkConstants.vehicleA4RepairTime);
        A5 = new VehiclePerformance(SimWorkConstants.vehicleA5Capacity, SimWorkConstants.vehicleA5Speed,
                SimWorkConstants.vehicleA5FailureChance, SimWorkConstants.vehicleA5RepairTime);

        rowOfVehicleA = new RowOfVehicles();
        rowOfVehicleB = new RowOfVehicles();

        roadAB = new NonJumpInLineRoad(SimWorkConstants.roadDistanceAB);
        roadCA = new NonJumpInLineRoad(SimWorkConstants.roadDistanceCA);
    }

    @Override
    protected void preConfigurationChangedRun() {
        statisticsInstance = new Statistics();

        switch (getActualConfigurationNumber()) {
            case 0:
                vehicles = new LinkedList<>();
                vehicles.add(new Vehicle(A1, seedRenerator.getSeed(), "A1"));
                vehicles.add(new Vehicle(A2, seedRenerator.getSeed(), "A2"));
                vehicles.add(new Vehicle(A3, seedRenerator.getSeed(), "A3"));
                vehicles.add(new Vehicle(A4, seedRenerator.getSeed(), "A4"));
                break;
            case 1:
                vehicles = new LinkedList<>();
                vehicles.add(new Vehicle(A1, seedRenerator.getSeed(), "A1"));
                vehicles.add(new Vehicle(A3, seedRenerator.getSeed(), "A3"));
                vehicles.add(new Vehicle(A5, seedRenerator.getSeed(), "A5"));
                break;
            case 2:
                vehicles = new LinkedList<>();
                vehicles.add(new Vehicle(A2, seedRenerator.getSeed(), "A2"));
                vehicles.add(new Vehicle(A3, seedRenerator.getSeed(), "A3"));
                vehicles.add(new Vehicle(A4, seedRenerator.getSeed(), "A4"));
                break;
        }

        for (Vehicle vehicle : vehicles) {
            statisticsInstance.addVehicle(vehicle);
        }

        statisticsInstance.setRowA(rowOfVehicleA);
        statisticsInstance.setRowB(rowOfVehicleB);
    }

    @Override
    protected void preReplicationRun() {
        for (Vehicle vehicle : vehicles) {
            vehicle.reset(seedRenerator.getSeed());
        }

        rowOfVehicleA.reset();
        rowOfVehicleB.reset();

        roadAB.reset();
        roadCA.reset();

        machineAWorking = false;
        machineBWorking = false;

        transported = 0;
        taken = 0;
        
        switch (getConfigurationCount()) {
            case 0:
                statisticsInstance.setVehicleCount(4);
                break;
            case 1:
                statisticsInstance.setVehicleCount(3);
                break;
            case 2:
                statisticsInstance.setVehicleCount(4);
                break;
        }
    }

    @Override
    protected void postReplicationRun() {
        statisticsInstance.replicationEnd(getSimTime());

        postRepStatisticsToGUI();
    }

    @Override
    protected void postConfigurationChangedRun() {
        System.out.println("Average simulation time: " + statisticsInstance.getSimTime() / 60);
        double[] confidenceInterval = statisticsInstance.getSimTimeIS(0.9);
        System.out.println("90% confidence interval of average simulation time: <" + confidenceInterval[0] / 60 + "," + confidenceInterval[1] / 60 + ">");

        System.out.println("Average sum waiting time in row A: " + statisticsInstance.getStatisticsAvgSumWaitingRowATotal() / 60);
        System.out.println("Average sum waiting time in row B: " + statisticsInstance.getStatisticsAvgSumWaitingRowBTotal() / 60);

        System.out.println("Average waiting time in row A: " + statisticsInstance.getStatisticsAvgTimeOfWaitingRowATotal());
        System.out.println("Average waiting time in row B: " + statisticsInstance.getStatisticsAvgTimeOfWaitingRowBTotal());

        System.out.println("Average row A size : " + statisticsInstance.getStatisticsAvgRowASizeTotal());
        System.out.println("Average row B size : " + statisticsInstance.getStatisticsAvgRowBSizeTotal());
        System.out.println("");

        postRepStatisticsToGUI();
    }

    @Override
    protected boolean customFinishCondition() {
        return transported < SimWorkConstants.demand;
    }

    @Override
    protected AbstrEvent initStartSimulationEvent() {
        return new StartSimulationEvent(0, this);
    }

    @Override
    public void simulationDelayRun() {
        runStatisticsToGUI();
    }

    private void runStatisticsToGUI() {
        for (ISimGraphics GUI : GUIOutputs) {
            GUI.setSimTime(getSimTime());

            double[] temp = getProgressAMachine();
            if (rowA == null) {
                GUI.setProgressA(0, 0, "", new Double(0));
            } else {
                GUI.setProgressA(temp[0], temp[1], rowA.getName(), rowA.getTimeOfAction(getSimTime()));
            }
            temp = getProgressBMachine();
            if (rowB == null) {
                GUI.setProgressB(0, 0, "", new Double(0));
            } else {
                GUI.setProgressB(temp[0], temp[1], rowB.getName(), rowB.getTimeOfAction(getSimTime()));
            }

            for (Vehicle car : vehicles) {
                GUI.setCarInformation(car.getName(), car.getWaitingA(), car.getWaitingB(),
                        car.getAvgTimeOfWaitingA(), car.getAvgTimeOfWaitingB(),
                        statusConvertor(car.getStatus()), car.getTimeOfAction(getSimTime()));
            }
        }
    }

    private void postRepStatisticsToGUI() {
        for (ISimGraphics GUI : GUIOutputs) {
            GUI.setMoveVehicles(statisticsInstance.getMoveVehicle());
            GUI.setReplicationNumber(statisticsInstance.getReplicationNumber());
            GUI.setGraphDataset(statisticsInstance.getDataSet());
            GUI.setAvgSimulationTime(statisticsInstance.getSimTime());
            double[] temp = statisticsInstance.getSimTimeIS(SimWorkConstants.confidenceLevel);
            GUI.setConfidenceIntervalOfSimulationTime(temp[0], temp[1]);

            GUI.setSumWaitingTimeInRowA(statisticsInstance.getStatisticsAvgSumWaitingRowATotal());
            GUI.setSumWaitingTimeInRowB(statisticsInstance.getStatisticsAvgSumWaitingRowBTotal());

            GUI.setAvgRowASize(statisticsInstance.getStatisticsAvgRowASizeTotal());
            GUI.setAvgRowBSize(statisticsInstance.getStatisticsAvgRowBSizeTotal());

            GUI.setAvgWaitingTimeRowA(statisticsInstance.getStatisticsAvgTimeOfWaitingRowATotal());
            GUI.setAvgWaitingTimeRowB(statisticsInstance.getStatisticsAvgTimeOfWaitingRowBTotal());
        }
    }

    private String statusConvertor(VehicleStatusEnum paStatus) {
        switch (paStatus.toString()) {
            case "RowA":
                return "Row A";
            case "RowB":
                return "Row B";
            case "ActionA":
                return "Service A";
            case "ActionB":
                return "Service B";
            case "OnWayAB":
                return "Way A-B";
            case "OnWayBC":
                return "Way B-C";
            case "OnWayBCFail":
                return "B-C with fail";
            case "OnWayCA":
                return "Way C-A";
            default:
                return "Out of sim";
        }
    }

    public void comeRowA(Vehicle paVehicle) {
        if (taken >= SimWorkConstants.demand) {
            paVehicle.setStatus(VehicleStatusEnum.OutOfSimulation, null, getSimTime());
            return;
        }

        statisticsInstance.carStay(getSimTime());

        rowOfVehicleA.addVehicle(paVehicle, getSimTime());
        paVehicle.setStatus(VehicleStatusEnum.RowA, null, getSimTime());

        if (!machineAWorking) {
            addEvent(new StartServiceAEvent(SimWorkConstants.delayFromRowAToServiceA, this, paVehicle));
            machineAWorking = true;
        }
    }

    public void comeRowB(Vehicle paVehicle) {
        paVehicle.setStatus(VehicleStatusEnum.RowB, null, getSimTime());
        rowOfVehicleB.addVehicle(paVehicle, getSimTime());

        statisticsInstance.carStay(getSimTime());

        if (!machineBWorking) {
            addEvent(new StartServiceBEvent(SimWorkConstants.delayFromRowBToServiceB, this, paVehicle));
            machineBWorking = true;
        }
    }

    public double serviceA(Vehicle paVehicle) {
        double capacity = paVehicle.getPerformance().getCapacity();
        Vehicle current = rowOfVehicleA.getNextVehicle();
        rowOfVehicleA.removeFirstVehicle(getSimTime());
        current.stopTimeWaitingA(getSimTime());

        double actionTime;
        if (taken + capacity < SimWorkConstants.demand) {
            taken += capacity;
            setProgressA(getSimTime(), capacity, paVehicle);
            actionTime = capacity / SimWorkConstants.machineAPowerInMin;
        } else {
            double rest = SimWorkConstants.demand - taken;
            taken = SimWorkConstants.demand;
            setProgressA(getSimTime(), rest, paVehicle);
            actionTime = rest / SimWorkConstants.machineAPowerInMin;
        }

        paVehicle.setStatus(VehicleStatusEnum.ActionA, actionTime, getSimTime());
        return actionTime;
    }

    public double serviceB(Vehicle paVehicle) {
        double capacity = paVehicle.getPerformance().getCapacity();
        Vehicle current = rowOfVehicleB.getNextVehicle();
        rowOfVehicleB.removeFirstVehicle(getSimTime());
        current.stopTimeWaitingB(getSimTime());

        double actionTime;
        if (transported + capacity < SimWorkConstants.demand) {
            transported += capacity;
            setProgressB(getSimTime(), capacity, paVehicle);
            actionTime = capacity / SimWorkConstants.machineBPowerInMin;
        } else {
            double rest = SimWorkConstants.demand - transported;
            transported = SimWorkConstants.demand;
            setProgressB(getSimTime(), rest, paVehicle);
            actionTime = rest / SimWorkConstants.machineBPowerInMin;
        }

        paVehicle.setStatus(VehicleStatusEnum.ActionB, actionTime, getSimTime());
        return actionTime;
    }

    public double endServiceA(Vehicle paVehicle) {
        if (rowOfVehicleA.isEmpty()) {
            machineAWorking = false;
        } else {
            addEvent(new StartServiceAEvent(SimWorkConstants.delayFromRowAToServiceA, this, rowOfVehicleA.getNextVehicle()));
        }

        statisticsInstance.carMove(getSimTime());

        double travelTime = roadAB.addVehicle(getSimTime(), paVehicle.getPerformance());
        paVehicle.setStatus(VehicleStatusEnum.OnWayAB, travelTime, getSimTime());
        return travelTime;
    }

    public double endServiceB(Vehicle paVehicle) {
        if (rowOfVehicleB.isEmpty()) {
            machineBWorking = false;
        } else {
            addEvent(new StartServiceBEvent(SimWorkConstants.delayFromRowBToServiceB, this, rowOfVehicleB.getNextVehicle()));
        }

        double travelBCTime = SimWorkConstants.roadDistanceBC / paVehicle.getPerformance().getSpeed();
        if (paVehicle.carFail()) {
            travelBCTime += paVehicle.getPerformance().getRepairTime();
            paVehicle.setStatus(VehicleStatusEnum.OnWayBCFail, travelBCTime, getSimTime());
            statisticsInstance.carMove(getSimTime() + paVehicle.getPerformance().getRepairTime());
        } else {
            paVehicle.setStatus(VehicleStatusEnum.OnWayBC, travelBCTime, getSimTime());
            statisticsInstance.carMove(getSimTime());
        }

        return travelBCTime;
    }

    public double comeCPoint(Vehicle paVehicle) {
        double travelTime = roadCA.addVehicle(getSimTime(), paVehicle.getPerformance());
        paVehicle.setStatus(VehicleStatusEnum.OnWayCA, travelTime, getSimTime());
        return travelTime;
    }

    public void initialize() {
        for (Vehicle vehicle : vehicles) {
            addEvent(new StartWaitingRowAEvent(0, this, vehicle));
        }
    }

    public void addGUI(ISimGraphics GUI) {
        GUIOutputs.add(GUI);
    }

    public void setProgressA(double startTime, double max, Vehicle paRowA) {
        machineAWorkStartTime = startTime;
        actualMaxAProgress = max;
        rowA = paRowA;
    }

    public void setProgressB(double startTime, double max, Vehicle paRowB) {
        machineBWorkStartTime = startTime;
        actualMaxBProgress = max;
        rowB = paRowB;
    }

    public double[] getProgressAMachine() {
        if (!machineAWorking) {
            return new double[]{0, 0};
        }
        return new double[]{(getSimTime() - machineAWorkStartTime) * SimWorkConstants.machineAPowerInMin, actualMaxAProgress};
    }

    public double[] getProgressBMachine() {
        if (!machineBWorking) {
            return new double[]{0, 0};
        }
        return new double[]{(getSimTime() - machineBWorkStartTime) * SimWorkConstants.machineBPowerInMin, actualMaxBProgress};
    }

    void reset() {
        if (vehicles != null) {
            vehicles.clear();
        }
        if (rowOfVehicleA != null) {
            rowOfVehicleA.reset();
        }
        if (rowOfVehicleB != null) {
            rowOfVehicleB.reset();
        }
        if (roadAB != null) {
            roadAB.reset();
        }
        if (roadCA != null) {
            roadCA.reset();
        }
        machineAWorking = false;
        machineAWorkStartTime = 0;
        actualMaxAProgress = 0;
        machineBWorking = false;
        machineBWorkStartTime = 0;
        actualMaxBProgress = 0;
        if (statisticsInstance != null) {
            statisticsInstance.reset();
        }
        transported = 0;
        taken = 0;
    }

    public ArrayList<Double> getDataSet() {
        return statisticsInstance.getDataSet();
    }
}
