package library;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import org.apache.commons.io.IOUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonReader {
	boolean printHttpResponse;

	
    TrustManager[] trustAllCerts;

    private SSLContext sc;
    
    
    
    public tickerData getTicker(boolean printoutput, boolean localtime)  {
    	  
    	boolean error;
    	BigInteger now=BigInteger.valueOf(0);
		double last=0, buy=0, sell=0;
        tickerData temp = new tickerData(now,last,buy,sell);
        
        do {
        	error=false;
        	
	    	if(printoutput){
	    		if(localtime){
	    			lib.conprint2("Ticker: ",true);
	    		}else{
	    			lib.conprint2(">>> Ticker: ",false);
	    		}
	    	}		
		    		
	        String queryResult = null;
	        
	        do {
		      	  error=false;
		  		try {
			    	queryResult = IOUtils.toString(new URL(Global.tickerURL));
				} catch (Exception e1) {
					lib.conprint2("\n\t\tConnection error: " + e1,false);
					lib.conprint2("\n\t\tReconnecting in 5 seconds...\n",false);
					lib.writeLog("errorlog", "getTicker error A: " + e1 + "\n" + queryResult,true,true);
					lib.sleep(25);
					error=true;
				}
	        }while (error);    	
	
	        JSONParser parser=new JSONParser();
	        
	        try {
	            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
	            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");
	            
	            now = new BigInteger((String)(dataJson.get("now")));
	            
	            JSONObject lastJson = (JSONObject)dataJson.get("last");
	            String temp_string = (String)lastJson.get("value");
	            last = Double.parseDouble(temp_string);
	            
	            if(printoutput) lib.conprint2("$" + Double.toString(last) + " ",false);
	            
	             lastJson = (JSONObject)dataJson.get("buy");
	             temp_string = (String)lastJson.get("value");
	             buy = Double.parseDouble(temp_string);
	             
	             lastJson = (JSONObject)dataJson.get("sell");
	             temp_string = (String)lastJson.get("value");
	             sell = Double.parseDouble(temp_string);
	             
	             temp = new tickerData(now,last,buy,sell);
	             
	             if(printoutput) lib.conprint2("@ " + lib.getTime((now.divide(new BigInteger("1000000"))).longValue()) + "\n",false); // Trade IDs have timestamp in the first digits
	            
	         } catch (ParseException ex) {
	        	  lib.conprint2("Error: "+ ex,false);
				  lib.writeLog("errorlog", "getTicker error B: " + ex + "\n" + queryResult,true,true);
		          lib.sleep(25);
		          lib.conprint2(" Reconnecting...\n",false);
		          error=true;
	        }
	        
        }while (error);    	
        
        return temp;
    }

    
    public Double getLag()  {        
        
    	Double lag=(double) 0;
    	String queryResult = "";
    	try {
        	queryResult = IOUtils.toString(new URL(Global.lagURL));
        
        	JSONParser parser=new JSONParser();

            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");
            lag = Double.valueOf(dataJson.get("lag").toString())/1000000;
            
         } catch (Exception ex) {
            lib.error("Error while getting trading engine lag: " + ex + "\n" + queryResult);
        }   
        return lag;
    }
    
    public void initSSL() throws NoSuchAlgorithmException, KeyManagementException
    
    // This is unsafe for middle-man-attack
    {        
    	 trustAllCerts = new TrustManager[ ] { new X509TrustManager() {
    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	            return null;
    	        }
    	        public void checkClientTrusted(X509Certificate[] certs, String authType) {
    	        }
    	        public void checkServerTrusted(X509Certificate[] certs, String authType) {
    	        }
    	    } };
    	    
    	    // Install the all-trusting trust manager
    	 sc = SSLContext.getInstance("SSL");
    	    
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
    }
    

    
}
