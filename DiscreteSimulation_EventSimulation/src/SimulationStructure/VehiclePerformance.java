/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

/**
 *
 * @author korenciak.marek
 */
public class VehiclePerformance {

    private double capacity = -1;
    private double speed = -1;
    private double failureChance = -1;
    private double repairTime = -1;

    public VehiclePerformance(double paCapacity, double paSpeed, double paFailureChance, double paRepairTime) {
        capacity = paCapacity;
        speed = paSpeed;
        failureChance = paFailureChance;
        repairTime = paRepairTime;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed/60.0;
    }

    public double getFailureChance() {
        return failureChance;
    }

    public double getRepairTime() {
        return repairTime;
    }
}
