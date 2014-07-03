package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.OpenID;
import play.libs.OpenID.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.WS;
import models.OauthResponse;

public class OAuthAuthenticator extends Controller {

	private static final String CLIENT_ID = Play.application().configuration().getString("application.client_id");	
	private static final String CLIENT_SECRET = Play.application().configuration().getString("application.client_secret");
	private static final String REDIRECT_URI = "http://localhost:9000/oath2Callback";
	private static final String GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";
	
	public static Result login() {
	  
		String state = new BigInteger(130, new SecureRandom()).toString(32);
		session().clear();
		session("state", state);

		String oAuthURL = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_ID + "&response_type=code&scope=openid%20email&redirect_uri=" + REDIRECT_URI + "&state=" + state;
		return redirect(oAuthURL);
	 
	}

	public static Result logout() {

		session().clear();
	    return redirect(routes.OAuthAuthenticator.login());
	}
  
	public static Result oath2Callback(String state, String code) {
	  
		if (!state.equals(session("state"))) {  
			return ok("Error - state cookies don't match");
		}
		else { 
			postLoginDetails(code);
		}
		  
		return redirect(routes.Application.index()); 
	}
  
  public static void postLoginDetails(String code){
	  
	  String postContent = "code=" + code + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "redirect_uri=" + REDIRECT_URI + "grant_type=authorization_code"; 
	  
	  HttpClient client = new DefaultHttpClient();
	  HttpPost request = new HttpPost(GOOGLE_TOKEN_ENDPOINT);
	    
	    try {	
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
	    	nameValuePairs.add(new BasicNameValuePair("code", code));
	    	nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
	    	nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
	    	nameValuePairs.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
	    	nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));    
	    	request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	HttpResponse response = client.execute(request);
	    	
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));	
	    	com.google.gson.Gson gson = new com.google.gson.Gson();
	    	JsonFactory jsonFactory = new GsonFactory();
	    	String hashedIdToken = gson.fromJson(rd, OauthResponse.class).getId_token();
	    	GoogleIdToken googleIdToken = GoogleIdToken.parse(jsonFactory, hashedIdToken);
	    	rd.close();

			session("email", googleIdToken.getPayload().getEmail());
			session("subject", googleIdToken.getPayload().getSubject());
			session("webToken", hashedIdToken);
	    }
	    
	    catch (ClientProtocolException e) 
	    {
	    	System.out.println(e.toString());	
	    }
	    catch (IOException ioe)
	    {
	    	System.out.println(ioe.toString());
	    } 
  	}
}
