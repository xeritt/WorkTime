package org.job;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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

    private String proName;
    public void setTrayStatus(String status) {
        this.tray.setStatus(status);
    }
    public void setStatus(String status) {
        //this.setTrayStatus(status);
        this.status = status;
    }


    public Gui() {
        System.out.println("Gui const");
        tray = SystemTray.get();
        tray.installShutdownHook();

        proName = getProjectName();
        //Если уже запущено
        File file = new File("start");
        if (file.exists()){
            setTrayStatus(CONNECTED + " " + proName);
            setStatus(CONNECTED);
            setLogo(LOGO_START);
        } else {
            setTrayStatus(DISCONECTED + " " + proName);
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
        addPropMenu(menu);

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
                setTrayStatus(CONNECTED + " " + proName);
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
                setTrayStatus(DISCONECTED + " " + proName);
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

    private void exec(String scriptName, String toastName) {
       try {
            Process process = Runtime.getRuntime().exec(scriptName);
            String res = getResults(process);
            Toast.showToast(toastName, res, 5000);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getProjectName(){
        File project = new File("project.dat");
        if (project.exists()){
            try (FileReader reader = new FileReader(project); BufferedReader buffer = new BufferedReader(reader)) {
                return buffer.readLine();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
    private void addPropMenu(JMenu menu) {
        JMenu propMenu = new JMenu("Project");
        menu.add(propMenu);
        File projects = new File("projects");
        for (final File project : projects.listFiles()) {
            if (project.isDirectory()) {
                JMenuItem prop = new JMenuItem(project.getName());
                prop.setOpaque(true);
                prop.setBackground(Color.CYAN);

                prop.addActionListener(e -> {
                    if (status.equals(DISCONECTED)) {
                        proName = project.getName();
                        exec("./select.sh " + proName, "Select project");
                        System.out.println("Set " + proName);
                        setTrayStatus(status + " " + proName);
                    } else {
                        Toast.showToast("No Select project", "Stop current project", 5000);
                    }
                });
                propMenu.add(prop);
            }
        }
    }



}
