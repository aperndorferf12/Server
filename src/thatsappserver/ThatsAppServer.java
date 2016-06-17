/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thatsappserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Perndorfer
 */
public class ThatsAppServer {

    private static HashMap <String, Socket> clients;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try 
        {
            ServerSocket ss = new ServerSocket(1234);
            clients = new HashMap<String,Socket>();
            while(true)
            {
                Socket c = ss.accept();
                System.out.println("accept " + c.getInetAddress()+" "+ss.getInetAddress());
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String line = br.readLine();
                System.out.println("line: "+line);
                clients.put(line, c);
                MyThread mt = new MyThread(clients.get(line));
                mt.start();
                System.out.println("put and started");
            }
        }
            
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

    public static synchronized OutputStream getClientOutputStream(String number)
    {
        try 
        {
            System.out.println("number: "+number);
            System.out.println(clients.get(number).isClosed()+" conn "+clients.get(number).isConnected());
            
            return clients.get(number).getOutputStream();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
}