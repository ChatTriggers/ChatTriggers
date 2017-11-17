package com.chattriggers.ctjs.utils.console;

import com.chattriggers.ctjs.CTJS;
import jp.uphy.javafx.console.ConsoleApplication;
import jp.uphy.javafx.console.ConsoleView;
import lombok.Getter;
import net.minecraft.network.ThreadQuickExitException;

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
    private boolean shouldClear;

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
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

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
        error.printStackTrace(out);
    }

    public void showConsole(boolean show) {
        this.frame.setVisible(show);

        Color bg = new Color(29, 31, 33);
        Color fg = new Color(197, 200, 198);

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
