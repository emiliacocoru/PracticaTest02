package ro.pub.cs.systems.eim.practicaltest02;


import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, BitcoinData> data;

    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Practic", "ServerSocket error for port " + port);
        }

        this.data = new HashMap<>();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Log.i("Practic", "[SERVER THREAD] Waiting for a client invocation...");
                Socket clientSocket = serverSocket.accept();
                Log.i("Practic", "[SERVER THREAD] A connection request was received from " + clientSocket.getInetAddress() + ":" + clientSocket.getLocalPort());

                if (clientSocket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(this, clientSocket);
                    communicationThread.start();

                } else {
                    Log.e("Practic", "Null client socket");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Practic", "Error at server socket accept method");
            }
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e("Practic", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());

            }
        }
    }

    public synchronized HashMap<String, BitcoinData> getData() {
        return data;
    }

    public synchronized void setData(HashMap<String, BitcoinData> data) {
        this.data = data;
    }
}


