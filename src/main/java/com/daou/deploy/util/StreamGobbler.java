package com.daou.deploy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;

/**
 * Process 의 하위 스레드로 InputStream를 사용할때 hang 걸리는 현상 해결을 위한 클래스
 * 
 * @author eceris
 *
 */
@Log
public class StreamGobbler extends Thread {
    InputStream is;
    PrintWriter writer = null;
    StringBuffer output = new StringBuffer();

    public StreamGobbler(InputStream is, HttpServletResponse resp) throws IOException {
        this.is = is;
        if (resp != null) {
            this.writer = resp.getWriter();
        }
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                log.info(line);
                if (this.writer != null) {
                    writer.write(line + "\n");
                    writer.flush();
                }
                output.append(line + "\n");
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public String getResult() {
        if (this.writer != null) {
            this.writer.close();
        }
        return this.output.toString();
    }

}
