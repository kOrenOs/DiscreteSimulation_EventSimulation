/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Random;

import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class DistributionBounds {

    private ArrayList<DistributionInterface> distributionContainer;
    private ArrayList<Double> valueBounds;
    private double insertedCount = 0;

    public DistributionBounds() {
        distributionContainer = new ArrayList<>();
        valueBounds = new ArrayList<>();
    }

    public boolean insertDistribution(double paBoundValue, DistributionInterface paDistribution) {
        if(paBoundValue>=1 || paBoundValue<=0){
            return false;
        }
        if(insertedCount==1){
            return false;
        }
        if(insertedCount+paBoundValue>1){
            return false;
        }
        insertedCount+=paBoundValue;
        valueBounds.add(paBoundValue);
        distributionContainer.add(paDistribution);
        return true;
    }
       
    public ArrayList getDistributions(){
        return distributionContainer;
    }
    
    public ArrayList getBounds(){
        return valueBounds;
    }
    
    public double boundFreeCoeficient(){
        return 1-insertedCount;
    }
}
