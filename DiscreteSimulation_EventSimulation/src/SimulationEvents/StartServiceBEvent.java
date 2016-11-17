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
public class StartServiceBEvent extends AbstrSimEvents{
    
    public StartServiceBEvent(double paEventDurationTime, SimulationStructure.SimCore paCore, Vehicle paVehicle) {
        super(paEventDurationTime, paCore, paVehicle);
    }

    @Override
    public void execute() {
        double serviceBDuration = getSimCore().serviceB(getVehicle());
        getSimCore().addEvent(new EndServiceBEvent(serviceBDuration, getSimCore(), getVehicle()));
    }
}
