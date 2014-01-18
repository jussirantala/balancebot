package library;

public class Global {
	
	public static double version = 0.01;
	public static String tickerURL="https://data.mtgox.com/api/2/BTCUSD/money/ticker_fast";
	public static String walletInfoPath="money/info";
	public static String baseURL="https://data.mtgox.com/api/2/";
	public static String lagURL="https://data.mtgox.com/api/2/money/order/lag";

	public static double walletInUSD=0;
	public static double walletInBTC=0;
	
	public static library.tickerData ticker = new library.tickerData(null,0.0,0.0,0.0);
	
  	public static double[] balance = {0.0, 0.0};
  	
  	public static boolean printHttpResponse=false;
  	
  	public static String decimalSeparator=",";
  	
  	public static int interval=30;

  	public static double tradeFee=0.6;
  
	public static ApiKeys apiKey;
	  	
}
