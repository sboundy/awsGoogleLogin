package controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.*;
import play.mvc.*;
import util.AWSSecurityToken;
import util.ConsoleURL;
import views.html.*;

public class Application extends Controller {
	
	public static String DURATION = "900";
	public static String FILE_LOCATION = Play.application().configuration().getString("application.fileLocation");
	public static String ROLE_ARN = Play.application().configuration().getString("application.awsRole");
	
	@Security.Authenticated(Secured.class)
    public static Result index() {
	
        return ok(index.render("Your subscription is " + request().username()));
    }
	
	@Security.Authenticated(Secured.class)
    public static Result retrieveCreds(String arn) {
		
		
		AWSSecurityToken awsSecurityToken = new AWSSecurityToken();
		

		if (arn != null) {
			
			ROLE_ARN = arn;
		}
		
		try {
		
			Element element  = awsSecurityToken.getSTSResponse(session("webToken"), ROLE_ARN);
			session("secretKey", awsSecurityToken.getSecretKey(element));
			session("accessKey", awsSecurityToken.getAccessKey(element));
			session("sessionToken", awsSecurityToken.getSessionToken(element));

		}
		catch (ClientProtocolException cpe){}
		catch (IOException ie){}
		catch ( ParserConfigurationException pce){}
		catch (SAXException saxe){}

        return ok(credentials.render(request().username(), session().get("accessKey"), session().get("secretKey"), session().get("sessionToken"), DURATION));

	}
	
	
	@Security.Authenticated(Secured.class)
    public static Result updateAWSFile() {
		
		try {
			PrintWriter writer = new PrintWriter(FILE_LOCATION, "UTF-8");
			writer.println("[default]");
			writer.println("region = eu-west-1");
			writer.println("aws_access_key_id = " + session().get("accessKey"));
			writer.println("aws_secret_access_key = " + session().get("secretKey"));
			writer.println("aws_security_token = " + session().get("sessionToken"));
			writer.close();
		}
		catch (FileNotFoundException fnf) {}
		catch (UnsupportedEncodingException uee) {}
		
		return ok(updateFile.render(request().username(), session().get("accessKey"), session().get("secretKey"), session().get("sessionToken"), DURATION, FILE_LOCATION));
		
	}

	
	@Security.Authenticated(Secured.class)
    public static Result getPortalLogin() {
		
		ConsoleURL consoleURL = new ConsoleURL();
		String url = consoleURL.requestUrl(session().get("accessKey"), session().get("secretKey"), session().get("sessionToken"));	
		return ok(portalLogin.render(request().username(), session().get("accessKey"), session().get("secretKey"), session().get("sessionToken"), DURATION, FILE_LOCATION, url));

	}
}
