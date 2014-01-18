package library;

public class ApiKeys {

	private String privateKey,apiKey;
	
	public ApiKeys(String privateKey, String apiKey)
	{
	    this.privateKey=privateKey;
	    this.apiKey=apiKey;
	}

    public String getPrivateKey() {
        return this.privateKey;
    }

    public String getApiKey() {
        return this.apiKey;
    }

}
