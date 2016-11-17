/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simCoreAbstraction;

import java.util.LinkedList;

/**
 *
 * @author korenciak.marek
 */
public final class EventList {
    private LinkedList<AbstrEvent> listOfEvents;

    public EventList() {
        listOfEvents = new LinkedList<AbstrEvent>();
    }
    
    public void addEvent(double actualTime, AbstrEvent paEvent){
        if(actualTime > paEvent.getEventActionTime()){
            return;
        }
        int actualListSize = listOfEvents.size();
        int index = 0;
        for (AbstrEvent event : listOfEvents) {
            if(event.getEventActionTime()>paEvent.getEventActionTime()){
                listOfEvents.add(index, paEvent);
                return;
            }
            index++;
        }
        if(actualListSize == listOfEvents.size()){
            listOfEvents.add(paEvent);
        }
    }
    
    public AbstrEvent getNextEvent(){
        return listOfEvents.remove(0);
    }
    
    public boolean emptyList(){
        if(listOfEvents.size()==0){
            return true;
        }
        return false;
    }
}
