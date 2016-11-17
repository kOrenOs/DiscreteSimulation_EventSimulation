/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Random;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class EmpiricalDistribution implements DistributionInterface {

    private ArrayList<DistributionInterface> range = null;
    private ArrayList<Double> bounds = null;
    private Random decisionRandom = null;
    private Random[] partialRadnom = null;

    public EmpiricalDistribution(DistributionBounds paRange, SeedGenerator paGenerator) {
        range = paRange.getDistributions();
        bounds = paRange.getBounds();
        decisionRandom = new Random(paGenerator.getSeed());
        partialRadnom = new Random[range.size()];
        for (int i = 0; i < range.size(); i++) {
            partialRadnom[i] = new Random(paGenerator.getSeed());
        }
    }

    private int findRange() {
        double decisionVariable = decisionRandom.nextDouble();
        int indexChoosen = -2;
        Double valueChoosen = 0.0;
        for (int i = 0; i < partialRadnom.length; i++) {
            valueChoosen += bounds.get(i);
            if (valueChoosen > decisionVariable) {
                indexChoosen = i;
                break;
            }
        }
        if (indexChoosen == -1) {
            indexChoosen = 0;
        }
        return indexChoosen;
    }

    @Override
    public int nextInt() {
        int index = findRange();
        return range.get(index).nextInt();
    }

    @Override
    public double nextDouble() {
        int index = findRange();
        return range.get(index).nextDouble();
    }
}
