/*
CalculatorService.java
The service threads for the calculator server.
This class implements the calculator.
Request are mad in prefix notation eg
"+ 3 4"
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class CalculatorService extends Thread
{
    private Socket serverSocket = null;
    private String requestStr = null;
    private String outcome = null;
    private InputStream inputStream = null;
    private DataInputStream dataInputStream = null;


    public CalculatorService(Socket aSocket)
    {
        //Launch the calculator service thread
        //TODO
        serverSocket = aSocket;
    }

    //Retrieve the request from the socket
    public String retrieveRequest()
    {
        //TODO
        try
        {
            inputStream = serverSocket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            requestStr = dataInputStream.readUTF();
        }
        catch (IOException e)
        {
            System.out.println("Problem opening socket: " + e);
            //TODO kill this thread
        }
        return this.requestStr;
    }

    //Parse the request command and execute the calculation
    public boolean attendRequest()
    {
        //TODO
        //Hint: Use StringTokenizer for parsing the user command and a switch statement to deal with the
        //different operations
        boolean flagRequestAttended = false;

        String[] elements = requestStr.split(" ");

        switch(elements[0])
        {
            case "+":
            {
                //do some addition
                outcome = (Double.parseDouble(elements[1]) + Double.parseDouble(elements[2])) + "\\#";
                flagRequestAttended = true;
                break;
            }
            case "*":
            {
                //do some multiplication
                outcome = (Double.parseDouble(elements[1]) * Double.parseDouble(elements[2])) + "\\#";
                flagRequestAttended = true;
                break;
            }
        }

        return flagRequestAttended;
    }

    //Wrap and return service outcome
    public void returnServiceOutcome()
    {
        //TODO
    }

    public void run()
    {
        try
        {
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved:" + this.requestStr);

            //Attend the request
            boolean tmp = this.attendRequest();
            //TODO You stopped here.
            //Send back the outcome of the request
            if(!tmp)
            {
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            }
            this.returnServiceOutcome();
        }
        catch(Exception e)
        {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }
}