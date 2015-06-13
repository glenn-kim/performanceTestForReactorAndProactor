package org.nhnnext;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String runmode = (args.length >= 1)?args[0]:"";
        String portStr = (args.length >= 2)?args[1]:"0";
        int port = 0;
        try{
            port = Integer.parseInt(portStr);
        }catch (NumberFormatException e){
            runmode = "port error";
        }

        Server server = null;

        switch(runmode){
            case "reactor":
                server = new org.nhnnext.java.reactor.ServerImpl();
                break;
            case "proactor":
                break;
            case "port error":
                System.out.println("port should be integer.");
            default:
                System.out.println("Usage : <command> [reactor|proactor] <port number>");
                System.exit(1);
        }
        if(server != null)
            server.start(port);

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
