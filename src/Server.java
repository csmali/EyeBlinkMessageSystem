import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class Server {
	
	private String ip = "85.110.73.189:1453";
	
    // Hasta bildirim göndermek istediğinde bu metodu kullanır
	public boolean sendNotification(String qrCode, String message) {
		try {
			String url = "http://" + ip + "/patient/sendNotification.php";
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
    // Yeni bir hasta üye olacağında bu metodu kullanır
	public String registerPatient(String name, String qrCode) {
		try {
			String url = "http://" + ip + "/patient/registerPatient.php";
			URL urlObj = new URL(url);
			HttpURLConnection http;
			http = (HttpURLConnection) urlObj.openConnection();
			
			http.setRequestMethod("POST");
			
			String postParams = "name=" + name + "&qr=" + qrCode;
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

                        return response.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    // Hasta değiştiğinde vs. bu metod kullanılarak hastanın ismi değiştirilir
	public boolean changePatientName(String id, String newName) {
		try {
			String url = "http://" + ip + "/patient/changePatientName.php";
			URL urlObj = new URL(url);
			HttpURLConnection http;
			http = (HttpURLConnection) urlObj.openConnection();
			
			http.setRequestMethod("POST");
			
			String postParams = "id=" + id + "&newName=" + newName;
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
       
	
    // QR kodunu değiştireceğinde bu metod kullanılır
	public boolean changeQR(String id, String qrCode) {
		try {
			String url = "http://" + ip + "/patient/changeQR.php";
			URL urlObj = new URL(url);
			HttpURLConnection http;
			http = (HttpURLConnection) urlObj.openConnection();
			
			http.setRequestMethod("POST");
			
			String postParams = "id=" + id + "&qr=" + qrCode;
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
