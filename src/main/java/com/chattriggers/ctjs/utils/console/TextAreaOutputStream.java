//package com.chattriggers.ctjs.utils.console;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
////Credit to https://stackoverflow.com/users/8946/lawrence-dol
//public class TextAreaOutputStream extends OutputStream {
//
//// *************************************************************************************************
//// INSTANCE MEMBERS
//// *************************************************************************************************
//
//    private byte[]                          oneByte;                                                    // array for write(int val);
//    private Appender                        appender;                                                   // most recent action
//    private BufferedWriter                  bufferedWriter;
//
//    public TextAreaOutputStream(JTextArea txtara) {
//        this(txtara,1000);
//    }
//
//    public TextAreaOutputStream(JTextArea txtara, int maxlin) {
//        if(maxlin<1) { throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value="+maxlin+")"); }
//        oneByte=new byte[1];
//        appender=new Appender(txtara,maxlin);
//
//        clearLog();
//
//        try {
//            bufferedWriter = new BufferedWriter(new FileWriter("./logs/ctjs.log", true));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /** Clear the current console text area. */
//    public synchronized void clear() {
//        if(appender!=null) { appender.clear(); }
//    }
//
//    public synchronized void close() {
//        appender=null;
//    }
//
//    public synchronized void flush() {
//    }
//
//    public synchronized void write(int val) {
//        oneByte[0]=(byte)val;
//        write(oneByte,0,1);
//    }
//
//    public synchronized void write(byte[] ba) {
//        write(ba,0,ba.length);
//    }
//
//    public synchronized void write(byte[] ba,int str,int len) {
//        if(appender!=null) {
//            String string = bytesToString(ba, str, len);
//
//            appender.append(string);
//
//            try {
//                bufferedWriter.append(string);
//                bufferedWriter.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void clearLog() {
//        try {
//            BufferedWriter tmpWriter = new BufferedWriter(new FileWriter("./logs/ctjs.log"));
//            tmpWriter.write("");
//            tmpWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static private String bytesToString(byte[] ba, int str, int len) {
//        try {
//            return new String(ba,str,len,"UTF-8");
//        } catch(UnsupportedEncodingException thr) {
//            return new String(ba,str,len);
//        }
//    }
//
//// *************************************************************************************************
//// STATIC MEMBERS
//// *************************************************************************************************
//
//    static class Appender implements Runnable {
//        private final JTextArea             textArea;
//        private final int                   maxLines;                                                   // maximum lines allowed in text area
//        private final LinkedList<Integer>   lengths;                                                    // length of lines within text area
//        private final List<String>          values;                                                     // values waiting to be appended
//
//        private int                         curLength;                                                  // length of current line
//        private boolean                     clear;
//        private boolean                     queue;
//
//        static private final String         EOL1="\n";
//        static private final String         EOL2=System.getProperty("line.separator",EOL1);
//
//        Appender(JTextArea txtara, int maxlin) {
//            textArea =txtara;
//            maxLines =maxlin;
//            lengths  =new LinkedList<Integer>();
//            values   =new ArrayList<String>();
//
//            curLength=0;
//            clear    =false;
//            queue    =true;
//        }
//
//        private synchronized void append(String val) {
//            values.add(val);
//            if(queue) { queue=false; EventQueue.invokeLater(this); }
//        }
//
//        private synchronized void clear() {
//            clear=true;
//            curLength=0;
//            lengths.clear();
//            values.clear();
//            if(queue) { queue=false; EventQueue.invokeLater(this); }
//        }
//
//        // MUST BE THE ONLY METHOD THAT TOUCHES textArea!
//        public synchronized void run() {
//            if(clear) { textArea.setText(""); }
//            for(String val: values) {
//                curLength+=val.length();
//                if(val.endsWith(EOL1) || val.endsWith(EOL2)) {
//                    if(lengths.size()>=maxLines) { textArea.replaceRange("",0,lengths.removeFirst()); }
//                    lengths.addLast(curLength);
//                    curLength=0;
//                }
//                textArea.append(val);
//            }
//            values.clear();
//            clear =false;
//            queue =true;
//        }
//    }
//}
