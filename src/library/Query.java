package library;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Query {
	
	private final static String SIGN_HASH_FUNCTION = "HmacSHA512";
	private final static String ENCODING = "UTF-8";

	public static boolean  getInfo() {
		boolean response=true;
		
		if(Global.apiKey.getApiKey().isEmpty() || Global.apiKey.getPrivateKey().isEmpty()){
			lib.error("Can't get account info, because no API key is set!");
			response=false;
		}else{
		
	
	        HashMap<String, String> query_args = new HashMap<String, String>();
	        
	        double[] balanceArray = new double[3];
	
	        String queryResult = executeQuery(Global.walletInfoPath, query_args); 
	         /*Sample result
	         * {
	         *   "data": {
	         *       "Created": "yyyy-mm-dd hh:mm:ss",
	         *       "Id": "abc123",
	         *       "Index": "123",
	         *       "Language": "en_US",
	         *       "Last_Login": "yyyy-mm-dd hh:mm:ss",
	         *       "Login": "username",
	         *       "Monthly_Volume":                   **Currency Object**,
	         *       "Trade_Fee": 0.6,
	         *       "Rights": ['deposit', 'get_info', 'merchant', 'trade', 'withdraw'],
	         *       "Wallets": {
	         *           "BTC": {
	         *               "Balance":                  **Currency Object**,
	         *               "Daily_Withdraw_Limit":     **Currency Object**,
	         *               "Max_Withdraw":             **Currency Object**,
	         *               "Monthly_Withdraw_Limit": null,
	         *               "Open_Orders":              **Currency Object**,
	         *               "Operations": 1,
	         *           },
	         *           "USD": {
	         *               "Balance":                  **Currency Object**,
	         *               "Daily_Withdraw_Limit":     **Currency Object**,
	         *               "Max_Withdraw":             **Currency Object**,
	         *               "Monthly_Withdraw_Limit":   **Currency Object**,
	         *               "Open_Orders":              **Currency Object**,
	         *               "Operations": 0,
	         *           },
	         *           "JPY":{...}, "EUR":{...},
	         *           // etc, depends what wallets you have
	         *       },
	         *   },
	         *   "result": "success"
	         * }
	         */
	        
	         JSONParser parser=new JSONParser();
	         try {
	            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
	            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");  

	            Global.tradeFee=(Double)dataJson.get("Trade_Fee");
	          
	            JSONObject walletsJson = (JSONObject)dataJson.get("Wallets"); 
	            
	            JSONObject BTCwalletJson = (JSONObject)((JSONObject)walletsJson.get("BTC")).get("Balance");  
	        
	            String BTCBalance = (String)BTCwalletJson.get("value");
	                      
	            boolean hasDollars = true;
	      //      boolean hasEuros = true;
	            JSONObject USDwalletJson; //,EURwalletJson;
	            String USDBalance =""; // , EURBalance ="";
	
	            try{
	                 USDwalletJson = (JSONObject)((JSONObject)walletsJson.get("USD")).get("Balance"); 
	                 USDBalance = (String)USDwalletJson.get("value");
	            }
	            catch (Exception e)
	            {
	                hasDollars = false;
	            }
	            
	            /*
	            try{
	                 EURwalletJson = (JSONObject)((JSONObject)walletsJson.get("EUR")).get("Balance");
	                 EURBalance = (String)EURwalletJson.get("value");
	            }
	            catch (Exception e)
	            {
	                hasEuros = false;  
	            } */
	            
	            balanceArray[0] = Double.parseDouble(BTCBalance); //BTC
	            
	            if(hasDollars)
	                balanceArray[1] = Double.parseDouble(USDBalance); //USD
	            else 
	                balanceArray[1] = -1; //Account does not have USD wallet
	/*
	            if(hasEuros)
	                balanceArray[2] = Double.parseDouble(EURBalance); //EUR
	            else 
	                balanceArray[2] = -1; //Account does not have EUR wallet
	*/
	            
	         } catch (Exception ex) {
	        	 lib.error("Error in getInfo() : " + ex + "\n" + queryResult);            
	 			response=false;
	        }
	        
	        Global.balance= balanceArray;
		
		} // if api key set -- end
		
		return response;
    }
	
	
	private static String executeQuery(String path,HashMap<String, String> args) {
        String answer = "";
        boolean httpError = false;
        HttpsURLConnection connection=null;
        String nonce = String.valueOf(System.currentTimeMillis())+"000";
        try {
            // add nonce and build arg list
            args.put("nonce", nonce);     
            String post_data= buildQueryString(args);
            String hash_data = path + "\0" + post_data; //Should be correct

            // args signature with apache cryptografic tools
            String signature = signRequest(Global.apiKey.getPrivateKey(), hash_data);

            // build URL
            URL queryUrl = new URL( Global.baseURL + path); 
            // create and setup a HTTP connection
            connection = (HttpsURLConnection)queryUrl.openConnection();

            connection.setRequestMethod("POST"); 

            connection.setRequestProperty("User-Agent", "Advanced-java-client API v2");
            connection.setRequestProperty("Rest-Key", Global.apiKey.getApiKey());
            connection.setRequestProperty("Rest-Sign", signature.replaceAll("\n", ""));

            connection.setDoOutput(true);
            connection.setDoInput(true);

            //Read the response

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(post_data);
            os.close();

            BufferedReader br = null;

            if (connection.getResponseCode() >= 400) {
                httpError = true;//TODO , if HTTP error, do something else with output!
                br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                }
            else 
                br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String output;

            if(httpError)
                System.err.println("HTTP error. Post Data: "+post_data);
            if (Global.printHttpResponse)  lib.conprint3("Query to :" + path + " , HTTP response : \n"); //do not log unless is error > 400
            
            while ((output = br.readLine()) != null) {
                        if(Global.printHttpResponse)
                        	lib.conprint3(output);
                        answer+=output;
                    }  
        } 

        //Capture Exceptions
        catch (IllegalStateException ex) {
            lib.error("Error while executing query: " + ex);
        }
        catch (IOException ex) {
        	lib.error("Error while executing query: " + ex);
        }
            finally
        {
            //close the connection, set all objects to null
            connection.disconnect();
            connection = null;
        }
        return answer;        
}
	
    private static String buildQueryString(HashMap<String, String> args) {
        String result = new String();
        for (String hashkey : args.keySet()) {
            if (result.length() > 0) result += '&';
            try {
                result += URLEncoder.encode(hashkey, ENCODING) + "="
                        + URLEncoder.encode(args.get(hashkey), ENCODING);
            } catch (Exception ex) {
            	 lib.error("Error in buildQueryString: " + ex);            
            }
        }
        return result;
    }
	
    private static String signRequest(String secret, String hash_data) {
        String signature = "";
         try{
           Mac mac = Mac.getInstance(SIGN_HASH_FUNCTION);
           SecretKeySpec secret_spec = new SecretKeySpec(Base64.decodeBase64(secret), SIGN_HASH_FUNCTION);
           mac.init(secret_spec);
           signature = Base64.encodeBase64String(mac.doFinal(hash_data.getBytes()));
         }
         catch (Exception e){
        	 lib.error("Error in signRequest() : " + e);            
         }
         return signature;
     }
	
}
