package org.job;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import dorkbox.systemTray.SystemTray;

/**
 * dorkbox.systemTray.SystemTray
 * https://github.undefined.moe/dorkbox/SystemTray/tree/master/src9
 * https://github.com/dorkbox/SystemTray
 */

public class Gui {
    public final String DISCONECTED = "Pause";
    public final String CONNECTED = "Working";
    public final String LOGO_STOP = "/logo_offline.png";
    public final String LOGO_START = "/logo_online.png";
    private final SystemTray tray;

    private String status = DISCONECTED;

    public void setStatus(String status) {
        this.tray.setStatus(status);
        this.status = status;
    }

    public Gui() {
        System.out.println("Gui const");
        tray = SystemTray.get();
        tray.installShutdownHook();

        //Если уже запущено
        File file = new File("start");
        if (file.exists()){
            setStatus(CONNECTED);
            setLogo(LOGO_START);
        } else {
            setStatus(DISCONECTED);
            setLogo(LOGO_STOP);
        }
        setMenu();
    }

    public void setLogo(String logo) {
        Image image = Toolkit.getDefaultToolkit()
                .createImage(getClass().getResource(logo));
        tray.setImage(image);
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static String getResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.readLine();
    }
    public void setMenu() {
        System.out.println("Menu");
        JMenu menu = new JMenu("Main menu");
        //addPropMenu(menu);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> {
            Toast.showToast("Information", "Working Time.", 5000);
        });
        menu.add(about);

        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");

        start.addActionListener(e -> {
            try {
                if (status.equals(CONNECTED)) return;
                Process process = Runtime.getRuntime().exec("./run.sh");
                printResults(process);
                setStatus(CONNECTED);
                setLogo(LOGO_START);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menu.add(start);

        stop.addActionListener(e -> {
            try {
                if (status.equals(DISCONECTED)) return;
                Process process = Runtime.getRuntime().exec("./run.sh");
                printResults(process);
                setStatus(DISCONECTED);
                setLogo(LOGO_STOP);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menu.add(stop);

        JMenuItem time = new JMenuItem("Time");
        time.addActionListener(e -> {
            try {
                Process process = Runtime.getRuntime().exec("./alltime.sh");
                String res = getResults(process);
                Toast.showToast("Information", res, 5000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menu.add(time);

        JMenuItem clean = new JMenuItem("Clean");
        clean.addActionListener(e -> {
            try {
                Process process = Runtime.getRuntime().exec("./clean.sh");
                String res = getResults(process);
                setStatus(DISCONECTED);
                setLogo(LOGO_STOP);
                final JOptionPane optionPane = new JOptionPane(
                        "Do you understand?",
                        JOptionPane.QUESTION_MESSAGE,
                        JOptionPane.YES_NO_OPTION);

                JDialog dialog = optionPane.createDialog("Clean ??");
                dialog.setAlwaysOnTop(true);
                dialog.setModal(true);
                dialog.setVisible(true);

                int value = ((Integer)optionPane.getValue()).intValue();

                if (value == JOptionPane.YES_OPTION) {
                    Toast.showToast("Information", "Clean ok", 5000);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menu.add(clean);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
                System.exit(0);
        });
        menu.add(exit);

        tray.setMenu(menu);
    }

}
