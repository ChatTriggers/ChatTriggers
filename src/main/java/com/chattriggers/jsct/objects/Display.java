package com.chattriggers.jsct.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;

public class Display {
    private ArrayList<String> lines;
    @Getter @Setter
    private int renderX;
    @Getter @Setter
    private int renderY;
    @Getter @Setter
    private boolean shouldRender;

    public Display() {
        lines = new ArrayList<>();

        //TODO: Default render X & Y?
        renderX = 0;
        renderY = 0;

        shouldRender = false;
    }

    public void setLine(int lineNumber, String line) {
        lines.set(lineNumber, line);
    }

    public void addLines(String... lines) {
        Collections.addAll(this.lines, lines);
    }

    public void clearLines() {
        lines.clear();
    }

    public void render() {
        for (String line : lines) {
            //TODO: RENDER
        }
    }
}
