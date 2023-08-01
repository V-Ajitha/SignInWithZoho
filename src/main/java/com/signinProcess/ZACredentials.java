package com.signinProcess;


import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;


import org.json.JSONObject;

public class ZACredentials{
    URL url;
    HttpsURLConnection httpsURLConnection;
    Scanner scanner = null;
    private static String refreshTkn;
    protected static String accessTkn;
    private String clientID = "1000.SL3ST111GJL5H1T4JNKIOPHD9W8UDP";
    private String clientSecret = "3b4325b9238869b3d873ac33c7b080cccc2534305b";
    private String code;
    Connection con;
    String First_Name;
    String Email;
    String Last_Name;
    String Display_Name;
    Long ZUID;
    
    protected Boolean isFirstToken = false;
    
    protected String getCode() {
		return code;
	}

	private String getClientSecret(){
		return clientSecret;
	}
	private String getClientID() {
		return clientID;
	}
	public void setAccessTkn(String accessToken){
		this.accessTkn = accessToken;
	}
	private void setRef_token(String refreshToken) {
		this.refreshTkn = refreshToken;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    public String createAccessToken(String code){
    	
    	String urlString = "https://accounts.zoho.com/oauth/v2/token?client_id=%s&grant_type=authorization_code&client_secret=%s&redirect_uri=%s&code=%s";
    	String redirectUrl = "http://localhost:8686/SpringSignin/SigninProcess";
       	    try{
			    URL url = new URL(String.format(urlString, getClientID(), getClientSecret(), redirectUrl, code));
			    HttpURLConnection connector = (HttpURLConnection) url.openConnection();
			    connector.setRequestMethod("POST");
			    Scanner scan = new Scanner(connector.getInputStream());
			    String res = scan.nextLine();
			    System.out.println("response: " + res);
			    JSONObject jsonObj = new JSONObject(res);
			    setAccessTkn(jsonObj.getString("access_token"));
				setRef_token(jsonObj.getString("refresh_token"));
//			    System.out.println("Access Token: " + jsonObj.getString("access_token"));
//				
//				System.out.println("Refresh Token: " + jsonObj.getString("refresh_token"));
				 
		}
        catch (Exception e){
            System.out.println("Error while creating access token....");
            e.printStackTrace();
        }
        return accessTkn;
    }
    
    public JSONObject getUserDetails() {
//    	System.out.println("called getUserDetails");
    	JSONObject jsonObj = null;
        try {
            URL url = new URL("https://accounts.zoho.com/oauth/user/info");
            HttpURLConnection connector = (HttpURLConnection) url.openConnection();
            connector.setRequestMethod("GET");
            connector.setRequestProperty("Authorization","Bearer " + ZACredentials.accessTkn);

            Scanner scan = new Scanner(connector.getInputStream());

            String response = scan.nextLine();

            jsonObj = new JSONObject(response);
            System.out.println("JSON object: " + jsonObj);
            First_Name = jsonObj.getString("First_Name");
            Email = jsonObj.getString("Email");
            Last_Name = jsonObj.getString("Last_Name");
            Display_Name = jsonObj.getString("Display_Name");
            ZUID = jsonObj.getLong("ZUID");
            return jsonObj;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getUserDetails");
        }
        return jsonObj;
    }
    public void storeDetails() {
    	 try {
         	String username = "Aji";
         	String password = "Jerry$001";
         	
             Class.forName("com.mysql.cj.jdbc.Driver");
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpringSignin",username,password);
             String query = "insert into storeDetails(First_Name,Email,Last_Name,Display_Name,ZUID) values(?,?,?,?,?)";
             PreparedStatement pst = con.prepareStatement(query);
             pst.setString(1, First_Name);
             pst.setString(2, Email);
             pst.setString(3, Last_Name);
             pst.setString(4, Display_Name);
             pst.setLong  (5, ZUID);
             pst.executeUpdate();
         } 
         catch (Exception e) {
        	 System.out.println("Error in storeDetails..");
             e.printStackTrace();
         }
    }
}
