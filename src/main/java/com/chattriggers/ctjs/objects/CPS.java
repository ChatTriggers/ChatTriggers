package com.chattriggers.ctjs.objects;

import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class CPS {
    private Long sysTime;

    private ArrayList<Integer> leftClicks;
    private ArrayList<Integer> rightClicks;
    private ArrayList<Double> leftClicksAverage;
    private ArrayList<Double> rightClicksAverage;

    /**
     * Gets the maximum left clicks.
     *
     * @return Maximum left clicks.
     */
    @Getter private int leftClicksMax;

    /**
     * Gets the maximum right click.
     *
     * @return Maximum right clicks.
     */
    @Getter private int rightClicksMax;

    public CPS() {
        this.sysTime = Minecraft.getSystemTime();
        this.leftClicks = new ArrayList<>();
        this.rightClicks = new ArrayList<>();
        this.leftClicksAverage = new ArrayList<>();
        this.rightClicksAverage = new ArrayList<>();
        this.leftClicksMax = 0;
        this.rightClicksMax = 0;
    }

    /**
     * Gets the left clicks per second at any particular instant.
     *
     * @return Left clicks per second.
     */
    public int getLeftClicks() {
        return leftClicks == null ? 0 : leftClicks.size();
    }

    /**
     * Gets the average left clicks per second.
     *
     * @return Average left click per second.
     */
    public int getLeftClicksAverage() {
        if (leftClicksAverage == null || leftClicksAverage.size() == 0) return 0;

        Double clicks = 0.0;
        for (Double click : leftClicksAverage) clicks += click;
        return (int) Math.round(clicks / leftClicksAverage.size());
    }

    /**
     * Gets the right clicks per second at any particular instant.
     *
     * @return Right click per second.
     */
    public int getRightClicks() {
        return rightClicks == null ? 0 : rightClicks.size();
    }

    /**
     * Gets the average right clicks per second.
     *
     * @return Average right clicks per second.
     */
    public int getRightClicksAverage() {
        if (rightClicksAverage == null || rightClicksAverage.size() == 0) return 0;

        Double clicks = 0.0;
        for (Double click : rightClicksAverage) clicks += click;
        return (int) Math.round(clicks / rightClicksAverage.size());
    }

    public void clickCalc() {
        if (sysTime == null) return;

        while (Minecraft.getSystemTime() > sysTime + 50L) {
            sysTime += 50L;

            decreaseClicks(leftClicks);
            decreaseClicks(rightClicks);

            leftClicksAverage.add((double) leftClicks.size());
            rightClicksAverage.add((double) rightClicks.size());

            if (leftClicksAverage.size() > 100) leftClicksAverage.remove(0);
            if (rightClicksAverage.size() > 100) rightClicksAverage.remove(0);

            if (leftClicksAverage.size() > 0 && leftClicksAverage.get(leftClicksAverage.size() - 1) == 0) {
                leftClicksAverage.clear();
                leftClicksMax = 0;
            }

            if (rightClicksAverage.size() > 0 && rightClicksAverage.get(rightClicksAverage.size() - 1) == 0) {
                rightClicksAverage.clear();
                rightClicksMax = 0;
            }
        }

        if (leftClicks.size() > leftClicksMax) leftClicksMax = leftClicks.size();
        if (rightClicks.size() > rightClicksMax) rightClicksMax = rightClicks.size();
    }

    public void addLeftClicks() {
        if (leftClicks != null) leftClicks.add(20);
    }

    public void addRightClicks() {
        if (rightClicks != null) rightClicks.add(20);
    }

    private void decreaseClicks(ArrayList<Integer> clicks) {
        if (clicks.size() > 0) {
            for (int i = 0; i < clicks.size(); i++) {
                clicks.set(i, clicks.get(i) - 1);
                if (clicks.get(i) == 0) clicks.remove(i);
            }
        }
    }
}
