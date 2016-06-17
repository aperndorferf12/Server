/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thatsappserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.net.www.http.ChunkedOutputStream;

/**
 *
 * @author Perndorfer
 */
public class MyThread extends Thread implements Runnable {

    private Socket s;
    OutputStream out = null;
    public MyThread(Socket s) {
        this.s = s;
    }

    
    @Override
    public void run() {
        InputStream in = null;
        try {
            in = s.getInputStream();
            System.out.println("in: " + in.toString());
            BufferedReader br;
            synchronized(in)
            {
                
                br = new BufferedReader(new InputStreamReader(in));
                
            }
            BufferedWriter bw = null; 
            String line = "";

            while ((line = br.readLine()) != null) {
                System.err.println(line);
                String[] split = line.split(";");

                out = ThatsAppServer.getClientOutputStream(split[0]);
                                       
                System.out.println("out: " + out.toString());
                String number = split[1];
                String msg = null, date = null, flag=null;
                flag = split[2];
                msg = split[3];
                date = split[4];
                bw = new BufferedWriter(new OutputStreamWriter(out));
                if (flag.equals("msg")) {
                    bw.write(number + ";msg;" + msg + ";" + date + "\r\n");
                    bw.flush();
                } else 
                {
                    Thread.sleep(1000);
                    byte[] bytes = new byte[16*1024];
                    
                    OutputStream fileOut = new FileOutputStream("C:\\Users\\Alexander\\Desktop\\Projekt\\Files\\"+msg);
                   
                    int count;
                    bw.write(number+";"+flag+";"+msg+";"+date+"\r\n");
                    bw.flush();
                    
                    while ((count = in.read(bytes))>0)
                    {
                        System.out.println("loop "+count);
                        fileOut.write(bytes,0,count);
                    }
                    
                    System.out.println(count);
                    System.out.println("wrote");
                    fileOut.close();
                    
                    File f = new File("C:\\Users\\Alexander\\Desktop\\Projekt\\Files\\"+msg);
                    FileInputStream fileIn = new FileInputStream(f);
                    
                    while ((count = fileIn.read(bytes)) > 0) 
                    {
                        out.write(bytes, 0, count);
                        System.out.println(count);
                    }
                    out.close();
                    fileIn.close();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
