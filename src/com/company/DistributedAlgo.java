package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Aggregation of various algorithms for distributed system
 */
public class DistributedAlgo
{
    public DistributedAlgo(int numMachines, int numData)
    {
        machines = new ArrayList<List<Integer>>(numMachines);
        int dataPerMachine = numData / numMachines;
        this.numData = numData;

        Random rnd = new Random(1234);
        for( int i = 0; i < numMachines; ++i ) {
            List<Integer> lst = new ArrayList<Integer>();
            for( int j = 0; j < dataPerMachine; ++j ) {
                lst.add(rnd.nextInt());
            }
            machines.add(lst);
        }
    }

    public int size()
    {
        return machines.size();
    }

    public void printMachine(int i)
    {
        assert( i < machines.size() && i >= 0 );
        System.out.print('[');
        for(int data : machines.get(i) ) {
            System.out.print(data);
            System.out.print(", ");
        }
        System.out.print("]\n");
    }

    /**
     * Find median over a group of machines
     */
    public int findMedian()
    {


        return 0;
    }

    private List<List<Integer>> machines;

    private int numData;
 }
