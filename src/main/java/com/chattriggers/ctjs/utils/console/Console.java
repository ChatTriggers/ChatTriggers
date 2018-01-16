package com.chattriggers.ctjs.utils.console;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.triggers.OnTrigger;
import io.sentry.Sentry;
import lombok.Getter;
import net.minecraft.network.ThreadQuickExitException;

import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;

public class Console {
    private JFrame frame;
    public PrintStream out;
    private TextAreaOutputStream taos;
    private ArrayList<Component> components;

    @Getter
    private static Console console;

    public Console() {
        this.frame = new JFrame("ct.js Console");
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        this.taos = new TextAreaOutputStream(textArea, 1000);
        textArea.setEditable(false);
        textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 15));
        JTextField inputField = new JTextField(1);
        inputField.setFocusable(true);

        inputField.setMargin(new Insets(5, 5, 5, 5));
        textArea.setMargin(new Insets(5, 5, 5, 5));

        components = new ArrayList<>();
        components.add(textArea);
        components.add(inputField);

        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Object toPrint = null;
                    boolean errorThrown = false;

                    String command = inputField.getText();
                    inputField.setText("");

                    try {
                        toPrint = CTJS.getInstance().getModuleManager().eval(command);
                    } catch (Exception error) {
                        if (!(error instanceof ThreadQuickExitException)) {
                            printStackTrace(error);
                            errorThrown = true;
                        }
                    }

                    if (toPrint != null) {
                        out.println(toPrint);
                    } else {
                        out.println(errorThrown ? "> " + command : command);
                    }
                }
            }
        });

        this.out = new PrintStream(taos);

        frame.add(new JScrollPane(textArea));
        frame.add(inputField, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(false);
        frame.setSize(800, 600);

        console = this;
    }

    public void clearConsole() {
        this.taos.clearLog();
        this.taos.clear();
    }

    public void printStackTrace(Throwable error) {
        Sentry.capture(error);
        error.printStackTrace(out);
    }

    public void printStackTrace(Throwable error, OnTrigger trigger) {
        Sentry.getContext().addTag(
            "methodName",
            trigger.getMethodName()
        );

        try {
            String body = CTJS.getInstance().getModuleManager().eval(trigger.getMethodName()).toString();

            Sentry.getContext().addExtra(
                "methodBody",
                body.replace("\n", "<br>")
            );
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        if (trigger.getOwningModule() != null) {
            Sentry.getContext().addTag(
                "moduleName",
                trigger.getOwningModule().getName()
            );

            if (trigger.getOwningModule().getMetadata() != null) {
                Sentry.getContext().addExtra(
                    "moduleMetadata",
                    trigger.getOwningModule().getMetadata().toString()
                );
            }
        }

        printStackTrace(error);
    }

    public void showConsole(boolean show) {
        this.frame.setVisible(show);

        Color bg = new Color(29, 31, 33);
        Color fg = new Color(197, 200, 198);

        if (CTJS.getInstance().getConfig().getCustomTheme()) {
            String bgColor = CTJS.getInstance().getConfig().getBg();
            bgColor = bgColor.substring(1, bgColor.length() - 1);
            String[] bgColors = bgColor.split(",");

            bg = new Color(RenderLib.limit255(Integer.parseInt(bgColors[0].trim())),
                    RenderLib.limit255(Integer.parseInt(bgColors[1].trim())),
                    RenderLib.limit255(Integer.parseInt(bgColors[2].trim())));

            String fgColor = CTJS.getInstance().getConfig().getFg();
            fgColor = fgColor.substring(1, fgColor.length() - 1);
            String[] fgColors = fgColor.split(",");

            fg = new Color(RenderLib.limit255(Integer.parseInt(fgColors[0].trim())),
                    RenderLib.limit255(Integer.parseInt(fgColors[1].trim())),
                    RenderLib.limit255(Integer.parseInt(fgColors[2].trim())));
        } else {
            switch (CTJS.getInstance().getConfig().getConsoleTheme()) {
                case "default.dark":
                    bg = new Color(21, 21, 21);
                    fg = new Color(208, 208, 208);
                    break;
                case "ashes.dark":
                    bg = new Color(28, 32, 35);
                    fg = new Color(199, 204, 209);
                    break;
                case "atelierforest.dark":
                    bg = new Color(28, 32, 35);
                    fg = new Color(199, 204, 209);
                    break;
                case "isotope.dark":
                    bg = new Color(0, 0, 0);
                    fg = new Color(208, 208, 208);
                    break;
                case "codeschool.dark":
                    bg = new Color(22, 27, 29);
                    fg = new Color(126, 162, 180);
                    break;
                case "gotham":
                    bg = new Color(10, 15, 20);
                    fg = new Color(152, 209, 206);
                    break;
                case "hybrid":
                    bg = new Color(29, 31, 33);
                    fg = new Color(197, 200, 198);
                    break;
                case "3024.light":
                    bg = new Color(247, 247, 247);
                    fg = new Color(74, 69, 67);
                    break;
                case "chalk.light":
                    bg = new Color(245, 245, 245);
                    fg = new Color(48, 48, 48);
                    break;
                case "blue":
                    bg = new Color(15, 18, 32);
                    fg = new Color(221, 223, 235);
                    break;
                case "slate":
                    bg = new Color(33, 36, 41);
                    fg = new Color(193, 199, 208);
                    break;
                case "red":
                    bg = new Color(26, 9, 11);
                    fg = new Color(231, 210, 212);
                    break;
                case "green":
                    bg = new Color(6, 10, 10);
                    fg = new Color(47, 227, 149);
                    break;
                case "aids":
                    bg = new Color(251, 251, 28);
                    fg = new Color(192, 20, 214);
                    break;
                default:
                    bg = new Color(21, 21, 21);
                    fg = new Color(208, 208, 208);
                    break;
            }
        }

        for (Component comp : this.components) {
            comp.setBackground(bg);
            comp.setForeground(fg);
        }

        this.frame.toFront();
        this.frame.repaint();
    }

    @Override
    public void finalize() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
