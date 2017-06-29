//Bismillah

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifications {

	public boolean sendNotification(String qrCode, String message) {
		try {
			String url = "http://10.2.234.134/sendNotification.php";
			URL urlObj = new URL(url);
			HttpURLConnection http;
			http = (HttpURLConnection) urlObj.openConnection();
			
			http.setRequestMethod("POST");
			
			String postParams = "qr=" + qrCode + "&msg=" + message;
			http.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(http.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			
			System.out.println("Response Code : " + http.getResponseCode());
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(http.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			System.out.println("Response : " + response.toString());
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean registerPatient(String qrCode) {
		try {
			String url = "http://10.2.234.134/registerPatient.php";
			URL urlObj = new URL(url);
			HttpURLConnection http;
			http = (HttpURLConnection) urlObj.openConnection();
			
			http.setRequestMethod("POST");
			
			String postParams = "qr=" + qrCode;
			http.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(http.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			
			System.out.println("Response Code : " + http.getResponseCode());
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(http.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			System.out.println("Response : " + response.toString());
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}