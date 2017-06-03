package com.johnwaltz.bots.chaoticprayer.ui;

/**
 * Class Info
 */
public class Info
{
    public int bonePh, boneCount;
    public String runTime, currentTask;

    /**
     * Constructor
     */
    public Info() {
        this.bonePh = 0;
        this.boneCount = 0;
        this.runTime = "";
        this.currentTask = "";
    }

    /**
     * @param bonePh
     * @param boneCount
     * @param runTime
     * @param currentTask
     */
    public Info(int bonePh, int boneCount, String runTime, String currentTask)
    {
        this.bonePh = bonePh;
        this.boneCount = boneCount;
        this.runTime = runTime;
        this.currentTask = currentTask;
    }
}
