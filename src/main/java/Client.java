/*
Client.java
A calculator client
Request an input from a user representing a simple addition or product operation,
then connects to the server to request the service of the operation,
and return the user the outcome of the operation.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public Socket clientSocket = null;
    private String userCommand = null; //The user command
    private String serviceOutcome = null; //The service outcome
    private OutputStreamWriter osw;
    private OutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;
    private InputStream inputStream = null;
    private StringBuilder responseString = null;
    private int serverPort = 6868;


    public Client()
    {
        //Initialise socket and connect to server
        try
        {
            clientSocket = new Socket(InetAddress.getLocalHost(), serverPort);
        } catch (IOException e)
        {
            System.out.println("IO Exception " + e);
            System.exit(1);
        }
    }

    public void requestService()
    {
        //Add terminator character \# to the service request message.
        if(userCommand.length() != 5) //TODO Change this to handle bad input
        {
            System.out.println("Invalid command");
            System.exit(1);
        }
        System.out.println("Client: Requesting calculator command for '" + userCommand + "'");
        userCommand = userCommand + "\\#";
    }

    public void reportServiceOutcome()
    {
        int length = responseString.length(); // xxx.xxx\# l = 9, want 7
        serviceOutcome = responseString.substring(0, length - 2);
        System.out.println("Client: Service outcome: " + serviceOutcome);
    }

    //Execute client
    public void execute()
    {
        //Prompt user for an operation
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Client: Please type your operation in prefix notation e.g. + 4 3");
        userCommand = myScanner.nextLine();

        try
        {
            //Request service
            this.requestService();
            outputStream = clientSocket.getOutputStream();

            //Do it with strings
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(userCommand);
            dataOutputStream.flush();
            dataOutputStream.close();


            /*
            //Do it with data
            byte[] data = userCommand.getBytes();
            outputStream.write(data);
            clientSocket.shutdownOutput();
            */


            //Await response
            inputStream = clientSocket.getInputStream();
            byte[] response = new byte[1024]; //I sure hope that's enough bytes
            int resp = 0;
            while((resp = inputStream.read(response)) != -1)
            {
                responseString.append((char)resp);
            }
            clientSocket.shutdownInput();
            outputStream.close();
            inputStream.close();

            //Report user outcome of service
            this.reportServiceOutcome();

            //Close the connection with the server
            this.clientSocket.close();
        }
        catch (Exception e)
        {
            //Raised if connection is refused or other technical issue
            System.out.println("Client: Exception " + e);
            System.exit(1);
        }

    }

    public static void main(String[] args)
    {
        Client client = new Client();
        client.execute();
        System.out.println("Client: Finished. ");
        System.exit(0);
    }
}
