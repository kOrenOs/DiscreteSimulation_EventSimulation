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
public class NonJumpInLineRoad {

    private double trafic = 0;
    private double roadDistnce = -1;

    public NonJumpInLineRoad(double paRoadSize) {
        roadDistnce = paRoadSize;
    }

    public double addVehicle(double simTime, VehiclePerformance paAnotherVehicle) {
        double minTimeOfVehicle = simTime + roadDistnce / paAnotherVehicle.getSpeed();

        if(minTimeOfVehicle > trafic){
            trafic = minTimeOfVehicle;
        }

        return trafic - simTime;
    }

    public void reset() {
        trafic = 0;
    }
}
