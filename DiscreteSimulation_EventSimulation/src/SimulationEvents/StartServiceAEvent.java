/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationEvents;

import SimulationStructure.Vehicle;

/**
 *
 * @author korenciak.marek
 */
public class StartServiceAEvent extends AbstrSimEvents {

    public StartServiceAEvent(double paEventDurationTime, SimulationStructure.SimCore paCore, Vehicle paVehicle) {
        super(paEventDurationTime, paCore, paVehicle);
    }

    @Override
    public void execute() {
        double serviceADuration = getSimCore().serviceA(getVehicle());
        getSimCore().addEvent(new EndServiceAEvent(serviceADuration, getSimCore(), getVehicle()));
    }
}
