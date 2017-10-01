package com.chattriggers.ctjs.utils.console;

import com.chattriggers.ctjs.CTJS;
import lombok.Getter;

import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

public class Console {
    private JFrame frame;
    public PrintStream out;
    private TextAreaOutputStream taos;

    @Getter
    private static Console console;

    public Console() {
        this.frame = new JFrame("ct.js Console");
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        this.taos = new TextAreaOutputStream(textArea, 1000);
        textArea.setEditable(false);
        textArea.setFont(new Font(null, Font.PLAIN, 15));
        JTextField inputField = new JTextField(1);
        inputField.setFocusable(true);

        inputField.setMargin(new Insets(5, 5, 5, 5));
        textArea.setMargin(new Insets(5, 5, 5, 5));

        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Object toPrint = null;
                    boolean shouldPrint = true;

                    try {
                        toPrint = CTJS.getInstance().getScriptEngine().eval(inputField.getText());
                    } catch (ScriptException error) {
                        printStackTrace(error);
                        shouldPrint = false;
                    }

                    if (toPrint != null && shouldPrint) {
                        out.println(toPrint);
                    }

                    inputField.setText("");
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
    }

    @Override
    public void finalize() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
