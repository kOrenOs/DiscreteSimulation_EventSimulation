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
public class EndServiceAEvent extends AbstrSimEvents{
    
    public EndServiceAEvent(double paEventDurationTime, SimulationStructure.SimCore paCore, Vehicle paVehicle) {
        super(paEventDurationTime, paCore, paVehicle);
    }

    @Override
    public void execute() {
        getSimCore().addEvent(new StartWaitingRowBEvent(getSimCore().endServiceA(getVehicle()), getSimCore(), getVehicle()));;
    }
}
