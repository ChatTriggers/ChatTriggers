package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;

import java.util.ArrayList;

public class CPS {
    public static CPS getInstance() {
        return instance;
    }

    private static CPS instance;

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
    @Getter
    private int leftClicksMax;

    /**
     * Gets the maximum right clicks.
     *
     * @return Maximum right clicks.
     */
    @Getter
    private int rightClicksMax;

    public CPS() {
        instance = this;

        this.sysTime = Client.getSystemTime();
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
     * @return Average left clicks per second.
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
     * @return Right clicks per second.
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

        while (Client.getSystemTime() > sysTime + 50L) {
            sysTime += 50L;

            decreaseClicks(leftClicks);
            decreaseClicks(rightClicks);

            leftClicksAverage.add((double) leftClicks.size());
            rightClicksAverage.add((double) rightClicks.size());

            limitAverage(leftClicksAverage);
            limitAverage(rightClicksAverage);

            clearOldLeft();
            clearOldRight();
        }

        findMax();
    }

    private void limitAverage(ArrayList<Double> average) {
        if (average.size() > 100)
            average.remove(0);
    }

    private void clearOldLeft() {
        if (leftClicksAverage.size() > 0 && leftClicksAverage.get(leftClicksAverage.size() - 1) == 0) {
            leftClicksAverage.clear();
            leftClicksMax = 0;
        }
    }

    private void clearOldRight() {
        if (rightClicksAverage.size() > 0 && rightClicksAverage.get(rightClicksAverage.size() - 1) == 0) {
            rightClicksAverage.clear();
            rightClicksMax = 0;
        }
    }

    private void findMax() {
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
