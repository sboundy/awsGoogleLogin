package util;

import java.net.URLEncoder;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;


public class ConsoleURL {
	
	String consoleURL = "https://console.aws.amazon.com";
	String signInURL = "https://signin.aws.amazon.com/federation";
	String loginURL = "";
	
	public String requestUrl(String accessKey, String secretKey, String sessionToken) {
	
	
		String sessionJson = String.format("{\"%1$s\":\"%2$s\",\"%3$s\":\"%4$s\",\"%5$s\":\"%6$s\"}", "sessionId", accessKey, "sessionKey", secretKey, "sessionToken", sessionToken);
		 
		try {
			String getSigninTokenURL = signInURL + "?Action=getSigninToken" + "&SessionType=json&Session=" + URLEncoder.encode(sessionJson, "UTF-8");
		
			URL url = new URL(getSigninTokenURL);
			URLConnection conn = url.openConnection ();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
		
			String returnContent = bufferReader.readLine();
			String signinToken = new JSONObject(returnContent).getString("SigninToken");
			String signinTokenParameter = "&SigninToken=" +  URLEncoder.encode(signinToken,"UTF-8");
			String destinationParameter = "&Destination=" + URLEncoder.encode(consoleURL,"UTF-8");
			bufferReader.close();
			
			loginURL = signInURL + "?Action=login" + signinTokenParameter + destinationParameter;	
		
		}
		
		catch(IOException io){
			
			System.out.println(io.toString());
			
		}
		
		catch(JSONException je){
			
			System.out.println(je.toString());
			
		}
		
		return loginURL;
	
		}

}
