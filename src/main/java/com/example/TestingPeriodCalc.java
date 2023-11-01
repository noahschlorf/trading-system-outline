package com.example;

public class TestingPeriodCalc {
    
    private final int totalLength;
    private final int numSteps;
    private final int inSampleFactor;
    private int inSampleLength;
    private int outSampleLength;

    public TestingPeriodCalc(int totalLength, int numSteps, int inSampleFactor) {
        this.totalLength = totalLength;
        this.numSteps = numSteps;
        this.inSampleFactor = inSampleFactor;
        
        calculatePeriods();
    }

    private void calculatePeriods() {
        this.outSampleLength = totalLength / (numSteps * (inSampleFactor + 1));
        this.inSampleLength = inSampleFactor * outSampleLength;
    }

    public void printTestingPeriods() {
        int currentBar = 0;
        for (int i = 0; i < numSteps; i++) {
            int inSampleStart = currentBar;
            int inSampleEnd = currentBar + inSampleLength;
            int outSampleStart = inSampleEnd;
            int outSampleEnd = outSampleStart + outSampleLength;

            currentBar = outSampleEnd; // Move to the next starting bar
            
            System.out.println("For your " + (i + 1) + "'s in-sample test use starting bar " + inSampleStart + " and ending bar " + inSampleEnd);
            System.out.println("For your " + (i + 1) + "'s out-of-sample test use starting bar " + outSampleStart + " and ending bar " + outSampleEnd);
        }
    }

    public static void main(String []args) {
        TestingPeriodCalc calculator = new TestingPeriodCalc(15000, 10, 3);
        calculator.printTestingPeriods();
    }
}
