package com.example.ibrah.first;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private String ip = "192.168.42.100";

    public String[] registerWithQR(String qrCode) {
        try {
            String url = "http://" + ip + "/carer/registerCarer.php";
            URL urlObj = new URL(url);
            HttpURLConnection http = null;
            http = (HttpURLConnection) urlObj.openConnection();
            http.setRequestMethod("POST");
            String params = "qr=" + qrCode + "&token=" + FirebaseInstanceId.getInstance().getToken();

            http.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            Log.i("PETHS", "Response Code : " + http.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.i("PETHS", "Response : " + response.toString());

            JSONObject json = new JSONObject(response.toString());

            String[] result = new String[2];
            result[0] = json.getString("id");
            result[1] = json.getString("patientName");

            return result;

        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public boolean confirmNotification(String notificationId, String qrCode) {
        try {
            String url = "http://" + ip + "/carer/confirmNotification.php";
            URL urlObj = new URL(url);
            HttpURLConnection http = null;
            http = (HttpURLConnection) urlObj.openConnection();
            http.setRequestMethod("POST");
            String params = "qr=" + qrCode + "&notification=" + notificationId;

            http.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            Log.i("PETHS", "Response Code : " + http.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.i("PETHS", "Response : " + response.toString());

            JSONObject json = new JSONObject(response.toString());

            return json.getBoolean("success");

        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        } catch (JSONException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean removeQR(String qrCode) {
        try {
            String url = "http://" + ip + "/carer/removeQR.php";
            URL urlObj = new URL(url);
            HttpURLConnection http = null;
            http = (HttpURLConnection) urlObj.openConnection();
            http.setRequestMethod("POST");
            String params = "qr=" + qrCode + "&token=" + FirebaseInstanceId.getInstance().getToken();

            http.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            Log.i("PETHS", "Response Code : " + http.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.i("PETHS", "Response : " + response.toString());

            JSONObject json = new JSONObject(response.toString());

            return json.getBoolean("success");

        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        } catch (JSONException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, String> getNotifications(String qrCode) {
        try {
            String url = "http://" + ip + "/carer/getNotifications.php";
            URL urlObj = new URL(url);
            HttpURLConnection http = null;
            http = (HttpURLConnection) urlObj.openConnection();
            http.setRequestMethod("POST");
            String params = "qr=" + qrCode;

            http.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            Log.i("PETHS", "Response Code : " + http.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.i("PETHS", "Response : " + response.toString());

            JSONObject json = new JSONObject(response.toString());
            JSONArray jsonArray = json.getJSONArray("notifications");
            Log.i("PETHS", "length : " + jsonArray.length());

            HashMap<String, String> result = new HashMap<String, String>();

            for (int i = 0; i < jsonArray.length(); i++) {
                //Log.i("PETHS", "girdi" + (String)jsonArray.getJSONObject(i).toString());
                JSONArray temp = jsonArray.getJSONArray(i);
                result.put(temp.getString(0), temp.getString(1));
                //Log.i("PETHS", "Object : " + jsonArray.getJSONObject(i).toString());
            }

            return result;

        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getPatients() {
        try {
            String url = "http://" + ip + "/carer/getPatients.php";
            URL urlObj = new URL(url);
            HttpURLConnection http = null;
            http = (HttpURLConnection) urlObj.openConnection();
            http.setRequestMethod("POST");
            String params = "token=" + FirebaseInstanceId.getInstance().getToken();

            http.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            Log.i("PETHS", "Response Code : " + http.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.i("PETHS", "Response : " + response.toString());

            JSONObject json = new JSONObject(response.toString());
            JSONArray jsonArray = json.getJSONArray("patients");
            Log.i("PETHS", "length : " + jsonArray.length());

            HashMap<String, String> result = new HashMap<String, String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                //Log.i("PETHS", "girdi" + (String)jsonArray.getJSONObject(i).toString());
                JSONArray temp = jsonArray.getJSONArray(i);
                result.put(temp.getString(0), temp.getString(1));
                //Log.i("PETHS", "Object : " + jsonArray.getJSONObject(i).toString());
            }

            return result;

        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
    }
}
