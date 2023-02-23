/*
CalculatorServer.java
The server main class.
This server provides a calculator service.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer
{
    private int thePort = 6868;
    private String theIPAddress = "127.0.0.1";
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    public CalculatorServer()
    {
        //TODO Initialise the socket and runs the service loop
        System.out.println("Server at " + theIPAddress + " is listening on port: " + thePort);
        try
        {
            serverSocket = new ServerSocket(thePort);
            this.executeServiceLoop();
        } catch (IOException e)
        {
            System.out.println("Problem opening socket");
            System.exit(1);
        }
    }

    //Runs the service loop
    public void executeServiceLoop()
    {
        try
        {
            //Service loop
            while(true) //This can only handle one client ATM without things getting screwy
            {
                //Listening for incoming client requests
                //TODO
                socket = serverSocket.accept(); // blocking call, this will wait until a connection is attempted on this port.

                //A new request has now arrived; create a service thread to attend the request
                CalculatorService calcServ = new CalculatorService(socket);
                calcServ.start();

                //The service thread will be responsible for sending back the outcome
                //so this service can now forget about that request and move on.
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        System.out.println("Server: Finished. ");
        System.exit(0);
    }

    public static void main(String[] args)
    {
        //TODO Run the server
        CalculatorServer calcServer = new CalculatorServer();
    }
}
