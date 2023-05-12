package org.job;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FileSystem {

    public static final String PROJECTS_DIR = "projects";
    public static final String PROJECT_DAT_FILE = "project.dat";
    public static final String START_FILE = "start";
    public static final String STARTTIME_FILE = "starttime";
    public static final String TIMES_DAT_FILE = "times.dat";
    public static final String SECONDS_DAT_FILE = "seconds.dat";
    public static final String ALLTIME_FILE = "alltime";

    static void saveToFile(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void appendToFile(String fileName, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.append(text);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String readFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.readLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public String getProjectName() {
        return readFile(PROJECT_DAT_FILE);
    }

    static public String getFile(String projectDir, String fileName) {
        return PROJECTS_DIR + "/" + projectDir + "/" + fileName;
    }

    static void run() {
        String project = getProjectName();
        //final String START_FILE_ = getFile(project, START_FILE);
        //final String STARTTIME_FILE_ = getFile(project, STARTTIME_FILE);
        final String TIMES_DAT_FILE_ = getFile(project, TIMES_DAT_FILE);
        final String SECONDS_DAT_FILE_ = getFile(project, SECONDS_DAT_FILE);
        //TIMES_DAT_FILE
        //SECONDS_DAT_FILE

        File file = new File(START_FILE);
        //timestamp=$(date +%s)
        long timestamp = getTimestamp();
        //curtime=`date +%Y-%m-%d_%H:%M:%S`
        String curtime = getCurtime("yyyy-mm-dd_hh:mm:ss");

        if (file.exists()) {
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

    @NotNull
    private static String getCurtime(String format) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(format);
        String curtime = dateFormat.format(date);
        return curtime;
    }

    private static long getTimestamp() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    public static boolean rmFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) return file.delete();
        return false;
    }

    public static long sumFile(String fileName) {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            long sum = 0;
            while ((line = reader.readLine()) != null) {
                sum += Long.parseLong(line);
            }
            return sum;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //return 0;
    }

    public static String convertSeconds(long sec) {
        return LocalTime.MIN.plusSeconds(sec).toString();
    }

    public static String allTime(){
        String project = getProjectName();
        final String SECONDS_DAT_FILE_ = getFile(project, SECONDS_DAT_FILE);
        long sum = sumFile(SECONDS_DAT_FILE_);
        return convertSeconds(sum);
    }

    public static void clean(){
        String project = getProjectName();
        final String TIMES_DAT_FILE_ = getFile(project, TIMES_DAT_FILE);
        final String SECONDS_DAT_FILE_ = getFile(project, SECONDS_DAT_FILE);
        List<String> files = Arrays.asList(
                TIMES_DAT_FILE_,
                SECONDS_DAT_FILE_,
                START_FILE,
                STARTTIME_FILE,
                ALLTIME_FILE
                );
        String curTime = getCurtime("yyyymmdd_hh_mm_ss");
        Zip.zipFiles(files, project + curTime + ".zip");
        files.stream().forEach(file -> rmFile(file));
        /*rmFile(TIMES_DAT_FILE_);
        rmFile(SECONDS_DAT_FILE_);
        rmFile(START_FILE);
        rmFile(STARTTIME_FILE);
        rmFile(ALLTIME_FILE);*/
    }



}
