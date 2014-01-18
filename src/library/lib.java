package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class lib {
	
	public static library.JsonReader json = new library.JsonReader(); 
	
	public static void printSettings(){
			lib.conprint3("Settings:\n"); 
			lib.conprint3("\tInterval:\t\t" + Global.interval + " minutes");
			lib.conprint3("\tDecimal separator:\t" + Global.decimalSeparator);
			lib.conprint3("\tPrint HTTP response:\t" + Global.printHttpResponse);
			lib.conprint3(""); 
	}
	
	private static int waittime,waitedtime;
	
	public static void waitWithInput(int wait) {
	     waittime=wait;
	 	BufferedReader br;
		waitedtime=0;

    	InputStreamReader isr = new InputStreamReader(System.in);
    	
    	do{
    		br = new BufferedReader(isr);

    	try{
    			
				do{
		    			sleep(10);
		    			waitedtime+=10;
		     	} while(!br.ready() && waitedtime<waittime);
				
		        if(br.ready()) handleInput(br.readLine());
		        
		}catch(Exception ex){
			error("Error while reading user input: " + ex);
			break;
		}
    		
    	}while(waitedtime<=waittime);
    	
    	return;
    }

   public static void handleInput(String text) {
	try{
	   
    	if(text.equals("stop")){
    		pause=true;
    		conprint3(">>> Paused: true");
    	}else if(text.equals("go")){
    		pause=false;
    		lib.conprint3(">>> Paused: false");
    	}else if(text.equals("cfg")){
    		conprint3(">>> Reloading cfg...");
    		readCfg("balancebot.cfg");
    		printSettings();
    	}else if(text.equals("ticker")){
    		lib.json.getTicker(true,false);
    	}else if(text.equals("waited")){
    		conprint3( ">>> " + waitedtime + " / " + waittime);
    	}else if(text.equals("skipwait")){
    		waitedtime=waittime;
       	}else if(text.equals("fee")){
    		conprint3(">>> Getting fee... ");
    		Query.getInfo();
    		conprint3(">>> " + Global.tradeFee + " %");
    	}else if(text.equals("quit")){ // It's safe to 	quit if we are here (in a wait)
    		conprint3(">>> Bye");
    		System.exit(0);
    	 }else if(text.equals("lag")){
    		double lag = lib.json.getLag();
    		conprint3(">>> Trading engine lag: " +lag + " seconds.");
    	}else if(text.equals("convert")){
    		conprint3(">>> Getting account balance...");
    		Query.getInfo();
    		conprint3(">>> Converting account balance...");
			Global.ticker=lib.json.getTicker(false,false);
           	Global.walletInUSD=Global.balance[1]+Global.balance[0]*Global.ticker.getLast()*(1-Global.tradeFee/100);
         	Global.walletInBTC=Global.balance[0]+(Global.balance[1]/Global.ticker.getLast()*(1-Global.tradeFee/100));
            lib.conprint3(">>> $" +  lib.round((float)Global.walletInUSD,4) + " or " + lib.round((float)Global.walletInBTC,4) + " BTC");
    	}else if(text.equals("balance")){
    		conprint3(">>> Getting account balance...");
    		Query.getInfo();
            lib.conprint3(">>> $" +  lib.round((float)Global.balance[1],4) + " | " + lib.round((float)Global.balance[0],4) + " BTC");
   		}else{
    		conprint3(">>> Unknown cmd: " + text);
    	}
		}catch (Exception e){
			error("Error while handling user input: " + e);
		} 
	 }

	
	public static void writeLog(String filename,String text, boolean newline, boolean printTimestamp) {
		
		String timeStamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + ": ";
		
		FileWriter out = null;
		try {
			out = new FileWriter(filename + ".txt",true);
		} catch (Exception e1) {
			lib.conprint("Error while opening logfile: " + e1);
		}
		
		try {
			out.write( (printTimestamp ? timeStamp : "") + text + (newline ? System.getProperty( "line.separator" ) : ""));
		} catch (Exception e) {
			lib.conprint("Error while writing logfile: " + e);
		}
		
		try {
			out.close();
		} catch (IOException e) {
			lib.conprint("Error while closing logfile: " + e);
		}	
		
	}
	

	public static void error(String txt){
		conprint(txt);
		writeLog("errorlog", txt,true,true);
	}
	
	
	public static String getTime(long timestamp){

		Date date = new Date(timestamp*1000L); // *1000 is to convert minutes to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		String formattedDate = sdf.format(date);
		
		return(formattedDate);
	}
	
	public static String getDate(long timestamp){

		Date date = new Date(timestamp*1000L); // *1000 is to convert minutes to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		String formattedDate = sdf.format(date);
	
		return(formattedDate);
	}
	
	
    public static float round(float d, int decimalPlace) {
    	BigDecimal bd=new BigDecimal(0);
    	try{    	
	        bd=  new BigDecimal(Float.toString(d));
	        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    	}catch(Exception e){
    		error("Error while rounding numbers: " + e);
    	}
        return bd.floatValue();
    }
    
    public static String timestamp(){
    	return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
    
    
	public static void conprint(String text){
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(timeStamp + ": " + text);
	}
	
	public static void conprint2(String text,boolean timestamp){
		String timeStamp="";
		
		if(timestamp) timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + ": ";
		
		System.out.print(timeStamp  + text);
	}
	
	public static void conprint3(String text){
		System.out.println(text);

	}
	
	public static void sleep(Integer multiply){
		
		if(multiply<1) multiply=1;
		
		try {
		    Thread.sleep(200*multiply);
		} catch(Exception ex) {
		    Thread.currentThread().interrupt();
		    return;
		}
	}

	
	public static boolean pause=false;
	
	public static void balanceCSV(){
			
		String filename;
		
		double btc,usd;

		filename="balance_history.csv";
		usd=Global.balance[1];
		btc=Global.balance[0];
	
		conprint2("Writing data into csv file: " + filename + "... ",true);

		FileWriter out = null;
		try {
			out = new FileWriter(filename,true);
		} catch (Exception e1) {
			error("Error while opening CSV file: " + e1);
		}
		
		String temp;
		try {
			out.write( getDate(Global.ticker.getTime()));
			out.write(";");
        	temp=Double.toString(Global.walletInUSD);
        	temp=temp.replace(".",Global.decimalSeparator);
			out.write(temp);
			out.write(";");
        	temp=Double.toString(Global.ticker.getLast());
        	temp=temp.replace(".",",");
			out.write(temp);
			out.write(";");
        	temp=Double.toString(btc);
        	temp=temp.replace(".",Global.decimalSeparator);
			out.write(temp);
			out.write(";");
        	temp=Double.toString(usd);
        	temp=temp.replace(".",Global.decimalSeparator);
			out.write(temp + "\n");
		} catch (Exception e) {
			error("Error while writing CSV file: " + e);
		}
		
		try {
			out.close();
			conprint3("OK");
		} catch (IOException e) {
			error("Error while closing CSV file: " + e);
		}
		
	}
	
	
    public static void  readCfg(String pathToJsonFile) {
        //see https://code.google.com/p/json-simple/wiki/DecodingExamples
        JSONParser parser=new JSONParser();

        String apiStr = lib.readFromFile(pathToJsonFile);
        try {
            JSONObject obj2=(JSONObject)(parser.parse(apiStr));
            
            Global.apiKey= new ApiKeys((String)obj2.get("mtgox_secret_key"), (String)obj2.get("mtgox_api_key"));
            
            Global.interval = Integer.valueOf((String) obj2.get("interval"));
      
            Global.decimalSeparator= (String)obj2.get("decimal_separator");
      
            Global.printHttpResponse = Boolean.valueOf((String) obj2.get("print_http_response"));
            
        } catch (ParseException ex) {
            error("Error while parsing config: " + ex);
        } catch (Exception ex) {
        	error("Error while loading config: " + ex);
        }
        	
    }
  
 public static String readFromFile(String path) {
        
        File file = new File(path);
         
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader = null;
         
        try {
 
            bufferedReader = new BufferedReader(new FileReader(file));
             
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                fileContent.append(text);
            }
 
        } catch (FileNotFoundException ex) {
        	error("Error in " + lib.class.getName() + ": " + ex);
        } catch (IOException ex) {
        	error("Error in " + lib.class.getName() + ": " + ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
            	error("Error in " + lib.class.getName() + ": " + ex);
            }
        } 
                 
        return fileContent.toString();
    }
    
}
