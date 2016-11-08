package com.daou.deploy.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.daou.deploy.util.StreamGobbler;

import lombok.extern.java.Log;

@Service
@Log
public class CommandService {
    public String executeCommand(HttpServletResponse resp, String command) {
        StringBuffer output = new StringBuffer();
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            InputStream stream = p.getInputStream();
            StreamGobbler gb = new StreamGobbler(stream, resp);
            gb.start();
            while (true) {
                if (!gb.isAlive()) {
                    String result = gb.getResult();
                    output.append(result);
                    p.waitFor();
                    break;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return output.toString();
    }
}
