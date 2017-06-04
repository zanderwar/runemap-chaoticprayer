package com.johnwaltz.bots.chaoticprayer.ui;

/**
 * Class Info
 */
public class Info
{
    public int bonePh, boneCount, levelsGained;
    public double xpPh;
    public String runTime, currentTask;

    /**
     * Constructor
     */
    public Info() {
        this.bonePh = 0;
        this.boneCount = 0;
        this.runTime = "";
        this.currentTask = "";
        this.xpPh = 0;
    }

    /**
     * @param bonePh
     * @param boneCount
     * @param runTime
     * @param currentTask
     */
    public Info(int bonePh, int boneCount, String runTime, String currentTask, double xpPh, int levelsGained)
    {
        this.bonePh = bonePh;
        this.boneCount = boneCount;
        this.runTime = runTime;
        this.currentTask = currentTask;
        this.xpPh = xpPh;
        this.levelsGained = levelsGained;
    }
}
