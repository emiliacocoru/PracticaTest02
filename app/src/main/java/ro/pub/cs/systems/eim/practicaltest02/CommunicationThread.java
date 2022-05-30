package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CommunicationThread extends Thread {

    private final Socket clientSocket;
    private HashMap<String, BitcoinData> data;
    private ServerThread serverThread;
    public CommunicationThread(ServerThread serverThread, Socket clientSocket) {
        this.serverThread = serverThread;
        this.clientSocket = clientSocket;
        this.data = serverThread.getData();
    }

    @Override
    public void run() {

        String clientResponse;
        BufferedReader bufferedReader;
        try {
            bufferedReader = Util.getReader(clientSocket);
            String[] req = bufferedReader.readLine().split(",");

            Log.d("Practic", "sunt in CommunicationThread");
            String moneyType = req[0];
            Log.i("Practic", "MoneyType: " + moneyType);
            BitcoinData bitcoinData = null;
            long currentTime = Calendar.getInstance().getTimeInMillis();

            if (data.containsKey(moneyType) && currentTime - Objects.requireNonNull(data.get(moneyType)).getCurrentTime() < 3600) {
                  bitcoinData = data.get(moneyType);
            } else {
                  HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/" + moneyType + ".json");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity == null) {
                        Log.e("Practic", "Null response from server");
                    }
                    String response = EntityUtils.toString(httpEntity);
                    Log.d("Practic", "updated " + response);

                    JSONObject content = new JSONObject(response);
                    String rate = content.getJSONObject("bpi").getJSONObject(moneyType).getString("rate");
                    String updated = content.getJSONObject("time").getString("updated");

                    bitcoinData = new BitcoinData(updated, rate, currentTime);
                    serverThread.setData(data);
            }

            PrintWriter printWriter = Util.getWriter(clientSocket);
            printWriter.println(bitcoinData.toString());
            printWriter.flush();
            clientSocket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
