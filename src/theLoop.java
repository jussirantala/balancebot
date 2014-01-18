import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import library.Global;
import library.Query;
import library.lib;

public class theLoop {

	private static void loop() {
		
		try {
			
			do {
			
				Global.ticker = lib.json.getTicker(true, true);
				
				if(Query.getInfo()){  // for account balance and fee-%
						    	        
					Global.walletInUSD=Global.balance[1]+Global.balance[0]*Global.ticker.getLast()*(1-Global.tradeFee/100);
					Global.walletInBTC= Global.balance[0]+Global.balance[1]/Global.ticker.getLast()*(1-Global.tradeFee/100);
									 		        
					lib.conprint("Balance: $" +  lib.round((float)Global.balance[1],4) + " | " + lib.round((float)Global.balance[0],4) + " BTC");
					lib.conprint3("\t  Balance converted: $" +  lib.round((float)Global.walletInUSD,4) + " or " + lib.round((float)Global.walletInBTC,4) + " BTC (with " + Global.tradeFee + " % fee)");
	
					lib.balanceCSV();
	
					lib.conprint("Waiting " + Global.interval + " minute" + (Global.interval>1 ? "s" : "") + "...");
					lib.waitWithInput(Global.interval*300);
					
				}else{
					lib.waitWithInput(25); // error while getting account info ... waiting 5 seconds
				}
				
			 }while(true);
			
		}catch(Exception e){
			lib.error("Error in loop(): " + e);
			e.printStackTrace();
    		lib.pause=true;
    		lib.conprint3(">>> Paused: true");
			lib.waitWithInput(5);
		}
		
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException  {
		
		lib.conprint3("Version:  " + Global.version + "\n");
		
		lib.readCfg("balancebot.cfg");
		
		lib.printSettings();
		
		lib.json.initSSL();	

		loop();

	}
	

}
