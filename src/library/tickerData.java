package library;

import java.math.BigInteger;

public class tickerData {
    private BigInteger  now;
    private double last ;
    private double buy ;
    private double sell ;
    
	public  tickerData(BigInteger now, double last,double buy,double sell) {
		this.now=now;
		this.last=last;
		this.buy=buy;
		this.sell=sell;
	}
	
	public double getLast(){
		return this.last;
	}
	
	public double getSell(){
		return this.sell;
	}
	
	public double getBuy(){
		return this.buy;
	}
	
	public BigInteger getNow(){
		return this.now;
	}
	
	public void setLast(Double lastt){
		this.last=lastt;
	}
	
	public void setNow(BigInteger nau){
		this.now=nau;
	}
	
	public Integer getTime(){
		return this.now.divide(new BigInteger("1000000")).intValue();
	}
}
	
