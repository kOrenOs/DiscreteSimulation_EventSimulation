/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simCoreAbstraction;

/**
 *
 * @author korenciak.marek
 */
public abstract class AbstrEvent {

    private double eventDurationTime;
    private double eventActionTime;

    public AbstrEvent(double paEventDurationTime) {
        eventDurationTime = paEventDurationTime;
    }

    public final void setActualSimTime(double paSimTime) {
        eventActionTime = paSimTime + eventDurationTime;
    }

    public final double getEventActionTime() {
        return eventActionTime;
    }

    public void execute() {
    }
}
