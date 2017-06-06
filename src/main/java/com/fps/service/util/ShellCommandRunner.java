package com.fps.service.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 2/1/17.
 */
@Component
public class ShellCommandRunner {

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            int status = p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();//.split("\\s+")[1];

    }


    public Integer executeForStatusCode(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            int status = p.waitFor();
            return status;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;

    }

    public List<String> getFilesAndFolders(String command) {

        List<String> files = new ArrayList<>();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            int status = p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                files.add(line);
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}
