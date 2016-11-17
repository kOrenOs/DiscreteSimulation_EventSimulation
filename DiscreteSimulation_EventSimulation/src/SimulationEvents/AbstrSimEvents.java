/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationEvents;

import SimulationStructure.Vehicle;
import simCoreAbstraction.AbstrEvent;

/**
 *
 * @author korenciak.marek
 */
public abstract class AbstrSimEvents extends AbstrEvent{
    
    private SimulationStructure.SimCore core;
    private Vehicle vehicle;
    
    public AbstrSimEvents(double paEventDurationTime, SimulationStructure.SimCore paCore, Vehicle paVehicle) {
        super(paEventDurationTime);
        core = paCore;
        vehicle = paVehicle;
    }
    
    protected SimulationStructure.SimCore getSimCore(){
        return core;
    }
    
    protected Vehicle getVehicle(){
        return vehicle;
    }
}
