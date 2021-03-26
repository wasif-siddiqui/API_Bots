import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jibble.pircbot.PircBot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

// extend pircbot to access certain methods
public class MyBotRon extends PircBot{
	
	//name the bot
	public MyBotRon() {
        this.setName("WasifQuoteBot7");
    } 
	
	//send quote
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// user enters quote
		if (message.equalsIgnoreCase("quote")) 
		{
			try {
				//call  getQuote function
				getQuote(channel, sender);
			} 
			catch (IOException e) {
				//catch errors
				e.printStackTrace();
				sendMessage(channel, sender + " error occured");	
			}
		}
	} //end send quote function
	
	//retrieve quotes from api
	public void getQuote(String channel, String sender) throws IOException {
		//make string url
		String quoteURL = "https://ron-swanson-quotes.herokuapp.com/v2/quotes";
		
		//make url object
		URL url1 = new URL(quoteURL); 
		HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
		conn1.setRequestMethod("GET");
		BufferedReader rd1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
		String json = rd1.readLine();
		
		//parse information
		String quote = parseJSON(json);
		
		//send the message
		sendMessage(channel, sender + ": Here's a quote from Ron Swanson: " + quote);	
	}
	
	//parse the information
	public String parseJSON(String json) {
		//create JsonArray to parse
		JsonArray info = (JsonArray) new JsonParser().parse(json);
		JsonElement quote = ((JsonElement)info.get(0)); // take quote from JsonArray

		String ronQuote = String.valueOf(quote);
		//get author

		return ronQuote; 
	}
}
