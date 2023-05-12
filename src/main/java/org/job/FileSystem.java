package org.job;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileSystem {

    public static final String PROJECTS_DIR = "projects";
    public static final String PROJECT_DAT_FILE = "project.dat";
    public static final String START_FILE = "start";
    public static final String STARTTIME_FILE = "starttime";
    public static final String TIMES_DAT_FILE = "times.dat";
    public static final String SECONDS_DAT_FILE = "seconds.dat";

    static void saveToFile(String fileName, String text){
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void appendToFile(String fileName, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.append(text);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static String readFile(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.readLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public String getProjectName(){
        return readFile(PROJECT_DAT_FILE);
    }

    static public String getFile(String projectDir, String fileName){
        return PROJECTS_DIR + "/" + projectDir + "/" + fileName;
    }
    static void run(){
        String project = getProjectName();
        //final String START_FILE_ = getFile(project, START_FILE);
        //final String STARTTIME_FILE_ = getFile(project, STARTTIME_FILE);
        final String TIMES_DAT_FILE_ = getFile(project, TIMES_DAT_FILE);
        final String SECONDS_DAT_FILE_ = getFile(project, SECONDS_DAT_FILE);
        //TIMES_DAT_FILE
        //SECONDS_DAT_FILE

        File file = new File(START_FILE);
        //timestamp=$(date +%s)
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        //curtime=`date +%Y-%m-%d_%H:%M:%S`
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        String curtime = dateFormat.format(date);

        if (file.exists()){
            long startstamp = Long.parseLong(readFile(START_FILE));
            //startstamp=`cat start`
            long seconds = timestamp - startstamp;
            //seconds="$((timestamp-startstamp))"
            String starttime = readFile(STARTTIME_FILE);
            //starttime=`cat starttime`
            appendToFile(TIMES_DAT_FILE_, starttime + " " + curtime + " " + seconds);
            //echo $starttime $curtime $seconds >> $TIMES_FILE
            appendToFile(SECONDS_DAT_FILE_, String.valueOf(seconds));
            //echo $seconds >> $SECONDS_FILE
            rmFile(START_FILE);
            rmFile(STARTTIME_FILE);
            //rm -f start starttime
        } else {
            //echo 'Start Time'
            saveToFile(START_FILE, String.valueOf(timestamp));
            //echo $timestamp > start
            saveToFile(STARTTIME_FILE, curtime);
            //echo $curtime > starttime
        }

    }

    private static boolean rmFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) return file.delete();
        return false;
    }


}
