package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.*;


public class AWSSecurityToken {
	
	public static String STS_ADDRESS = "https://sts.amazonaws.com/";
	public static String DURATION = "900";
	public static String secretKey = new String();
	public static String accessKey = new String();
	public static String sessionToken = new String();
	public static String FILE_LOCATION = Play.application().configuration().getString("application.fileLocation");
	
	public static String getSecretKey(Element firstCredentialsElement) {
		    
		NodeList awsSecretAccessKeyId = firstCredentialsElement.getElementsByTagName("SecretAccessKey");			    	   	
		Element awsSecretAccessKeyIdElement = (Element)awsSecretAccessKeyId.item(0);    
		secretKey = awsSecretAccessKeyIdElement.getChildNodes().item(0).getNodeValue().trim();
			    
		return secretKey;	    				
	}
	
	public static String getAccessKey(Element firstCredentialsElement) {
			
		NodeList awsAccessKeyId = firstCredentialsElement.getElementsByTagName("AccessKeyId");
		Element awsAccessKeyIdElement = (Element)awsAccessKeyId.item(0);
		accessKey = awsAccessKeyIdElement.getChildNodes().item(0).getNodeValue().trim();
	 
		return accessKey;
	}
			
	public static String getSessionToken(Element firstCredentialsElement) {
			
		NodeList awsSessionTokenId = firstCredentialsElement.getElementsByTagName("SessionToken");
		Element awsSessionTokenIdElement = (Element)awsSessionTokenId.item(0);
		sessionToken = awsSessionTokenIdElement.getChildNodes().item(0).getNodeValue().trim();
				
		return sessionToken;
	}
			
		
	public static Element getSTSResponse(String subjectSession, String arn) throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
			
		String getURL = STS_ADDRESS + "/?DurationSeconds=" + DURATION + "&Action=AssumeRoleWithWebIdentity&Version=2011-06-15&RoleSessionName=web-identity-federation&RoleArn=" + arn + "&WebIdentityToken=" + subjectSession; 	
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(getURL);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));	
		InputSource xmlinput = new InputSource(rd); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlinput); 
		NodeList credentials_node = doc.getElementsByTagName("Credentials");
		Node firstCredentials = credentials_node.item(0);
		
		return (Element) firstCredentials;
	 }
}
