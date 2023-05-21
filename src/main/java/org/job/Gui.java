package org.job;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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
            String infoFileName = FileSystem.getFile(getProjectName(), FileSystem.INFO_TXT_FILE);
            String info = FileSystem.readFile(infoFileName);
            Toast.showToast("Information", "Working Time. Project info:\n" + info, 5000);
        });
        menu.add(about);

        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");

        start.addActionListener(e -> {
            //try {
                if (status.equals(CONNECTED)) return;
                //Process process = Runtime.getRuntime().exec("./run.sh");
                //printResults(process);
                FileSystem.run();
                setTrayStatus(CONNECTED + " " + proName);
                setStatus(CONNECTED);
                setLogo(LOGO_START);
            //} catch (IOException ex) {
              //  throw new RuntimeException(ex);
            //}
        });
        menu.add(start);

        stop.addActionListener(e -> {
            //try {
                if (status.equals(DISCONECTED)) return;
                //Process process = Runtime.getRuntime().exec("./run.sh");
                //printResults(process);
                FileSystem.run();
                setStatus(DISCONECTED);
                setTrayStatus(DISCONECTED + " " + proName);
                setLogo(LOGO_STOP);
            //} catch (IOException ex) {
              //  throw new RuntimeException(ex);
            //}
        });
        menu.add(stop);

        JMenuItem time = new JMenuItem("Time");
        time.addActionListener(e -> {
            //try {
                //Process process = Runtime.getRuntime().exec("./alltime.sh");
                String res = FileSystem.allTime();//getResults(process);
                Toast.showToast("Information", res, 5000);
            //} catch (IOException ex) {
              //  throw new RuntimeException(ex);
            //}
        });
        menu.add(time);

        JMenuItem clean = new JMenuItem("Clean");
        clean.addActionListener(e -> {
           // try {
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
                    //Process process = Runtime.getRuntime().exec("./clean.sh");
                    //String res = getResults(process);
                    FileSystem.clean();
                    setStatus(DISCONECTED);
                    setLogo(LOGO_STOP);
                    Toast.showToast("Information", "Clean ok", 5000);
                }

            //} catch (IOException ex) {
            //    throw new RuntimeException(ex);
           // }
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

        JMenuItem add = new JMenuItem("New");
        add.addActionListener(e -> {
            newProject();
        });
        propMenu.add(add);

        File projects = new File("projects");
        if (!projects.exists()) return;
        for (final File project : projects.listFiles()) {
            if (project.isDirectory()) {
                JMenuItem prop = new JMenuItem(project.getName());
                prop.setOpaque(true);
                prop.setBackground(Color.CYAN);

                prop.addActionListener(e -> {
                    if (status.equals(DISCONECTED)) {
                        proName = project.getName();
                        //exec("./select.sh " + proName, "Select project");
                        FileSystem.saveToFile(FileSystem.PROJECT_DAT_FILE, proName);
                        Toast.showToast("Select project", proName, 5000);
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

    private static void newProject() {
        String msgString1 = "Enter project name (50 symbols)";
        String msgString2 = "Enter info about project";
        JTextField nameField = new JTextField(20);
        JTextArea infoField = new JTextArea(15, 10);

        Object[] array = {msgString1, nameField, msgString2, infoField};
        final JOptionPane optionPane = new JOptionPane(
                array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        JDialog dialog = optionPane.createDialog("New project");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setVisible(true);

        int value = ((Integer)optionPane.getValue()).intValue();

        if (value == JOptionPane.YES_OPTION) {
            String name = nameField.getText();
            String dirName = FileSystem.PROJECTS_DIR + FileSystem.fileSeparator + name;
            File file = new File(dirName);
            if (file.exists() || file.isDirectory()){
                Toast.showToast("Error create project", "File or Directory " + dirName +" is exists", 5000);
            } else {
                Toast.showToast("Create project", dirName, 5000);
                file.mkdirs();
            }
            String infoFileName = FileSystem.getFile(name, FileSystem.INFO_TXT_FILE);
            FileSystem.saveToFile(infoFileName, infoField.getText());

        }
    }


}
