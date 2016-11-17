/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Random;

import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class UniformDistribution implements DistributionInterface{
    
    private Random randomInstance = null;
    private int lowerBound = -1;
    private int uperBound = -1;

    public UniformDistribution(int paLowerBound, int paUperBound, SeedGenerator paGenerator) {
        randomInstance = new Random(paGenerator.getSeed());
        if(paUperBound< paLowerBound){
            lowerBound = paUperBound;
            uperBound = paLowerBound;
            return;
        }
        lowerBound = paLowerBound;
        uperBound = paUperBound;
    }
    
    @Override
    public int nextInt() {
        return ((Double)Math.floor(lowerBound + randomInstance.nextDouble()*(uperBound+1-lowerBound))).intValue();
    }

    @Override
    public double nextDouble() {
        return lowerBound + randomInstance.nextDouble()*(uperBound-lowerBound);
    }
}
