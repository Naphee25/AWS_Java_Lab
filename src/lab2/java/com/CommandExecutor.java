/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab2.java.com;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author halo93
 */
public class CommandExecutor implements Callable<String> {
    
    private final String command;

    public CommandExecutor(String command) {
        this.command = command;
    }
    
    public String execute() {
        final String os = System.getProperty("os.name");
        String commandTemplate = "cmd /c %s";
        if (os.contains("Linux")) {
            commandTemplate = "%s";
        }
        final String rs = execCmd(String.format(commandTemplate, command), os);
        return rs;
    }
    
    private String execCmd(String cmd, String os) {
        if (os.contains("Linux")) {
            return execCmdInLinux(cmd);
        } else if (os.contains("Windows")) {
            return execCmdInWindows(cmd);
        }
        return null;
    }
    
    private String execCmdInLinux(String cmd) {
        String rs = null;
        
        try {
            String[] extendedCmd = { "/bin/sh", "-c", cmd };
            Process p = Runtime.getRuntime().exec(extendedCmd);
            p.waitFor();
            InputStream is = p.getInputStream();
            Scanner sc = new Scanner(is).useDelimiter("\\A");
            rs = sc.hasNext() ? sc.next() : null;
            is.close();
            sc.close();
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(FindCommand.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return rs;
    }
    
    private String execCmdInWindows(String cmd) {
        String rs = null;
        try (InputStream is = Runtime.getRuntime().exec(cmd).getInputStream();
             Scanner sc = new Scanner(is).useDelimiter("\\A")) {
            rs = sc.hasNext() ? sc.next() : null;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FindCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }


    @Override
    public String call() throws Exception {
        return execute();
    }
}
