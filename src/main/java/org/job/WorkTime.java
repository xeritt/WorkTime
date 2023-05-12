package org.job;

public class WorkTime implements  Runnable {
    private final String[] args;
    private Gui gui = new Gui();
    public WorkTime(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) {
          Thread clientThread = new Thread(new WorkTime(args));
          clientThread.start();
        //FileSystem.saveToFile("test.txt", String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void run() {
        //gui.setStatus(gui.CONNECTED);
        //gui.setLogo(gui.LOGO_ONLINE);
    }
}