//imports
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jibble.pircbot.*;

public class MyBotWeather extends PircBot {

	//name the bot
    public MyBotWeather() {
        this.setName("WasifWeatherBot7");
    } 
    
    //send message
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    	//store message from user to content
        String content = message;
        
        //this counts the number of words inputed by user
        int wordCount = 0;
        
        //divide content into array with each word, split by spaces
        String command[] = content.split(" ");
        
        // number of words in user input
        wordCount = command.length;
        
        // if input valid or not
        boolean invalid = true;
        
        //if help is first word
        if (command[0].equalsIgnoreCase("help")) {
        	helpfunction(channel, sender);
        	invalid = false; // valid
        }
        
        //if weather is first word
        else if(command[0].equalsIgnoreCase("weather")) {
        	invalid = false; // valid
        	if(wordCount == 1) {
        		// output instructions again, user messed up
        		sendMessage(channel, sender + ": Please try again using the command format \"weather [cityname]\" or \"weather [zipcode]\"");
        	}
        	try {
				getWeather(channel, sender, command, wordCount); // send to get weather function
				
			//send error message if city or zipcode is not known 
			} 
        	catch (IOException e) {
					e.printStackTrace();
					sendMessage(channel, sender + ": The weather is not available, make sure you entered the city or zipcode correctly.");
			}
        }
        // they want a quote, myBotRon can handle this so don't do anything in weatherbot
        else if(command[0].equalsIgnoreCase("quote")) {
        	invalid = false; // valid
        }
        
        // invalid command
        else if (invalid && !message.contains("Here's a quote")) {
        	sendMessage(channel, sender + ": The command was not understood");
        	sendMessage(channel, sender + ": type \"help\" for instructions");
        }
      
    }//end function
    
    // instructions function
    public void helpfunction(String channel, String sender) {
    	sendMessage(channel, sender + ": To see temperature of a city, type: \"weather [city name]\" or \"weather [zipcode]\"");
    	sendMessage(channel, sender + ": To get a quote from Ron Swanson from the show \"Parks and Recreation,\" type \"quote\"");
    }
    
    //get temperature of city
    public void getWeather(String channel, String sender, String[] command, int wordCount) throws IOException {
    	
    	final String myKey = "&APPID=aea0205bc1ac44350e30bf50e4d4c21f";
    	
    	if(command[1].matches(".*\\d.*")) { // if second word is number (zipcode)
    		
    		//url
    		String myUrlZip = "http://api.openweathermap.org/data/2.5/weather?zip=";
    		
    		//zipcode from user
    		String userInputZip = command[1]; // second element
    		
    		//full website url
    		String weatherURL = myUrlZip + userInputZip + myKey;
    		
    		//create weather object
    		URL url = new URL(weatherURL); 
    		HttpURLConnection con = (HttpURLConnection) url.openConnection();
    		con.setRequestMethod("GET");
    		BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
    		String json = read.readLine(); // store json string in json
    		
    		//this function parses information from website
    		String message = parseJSON(json);
    		
    		//send temperature to user
    		sendMessage(channel, sender + message);
    	}
    	
    	// no numbers, its a city name
    	else {

    		//url
			String myUrlCity = "http://api.openweathermap.org/data/2.5/weather?q=";
			
			//city name inputed
			String userInput = command[1];
			
			//if 3 words entered, city has 2 words
			if(wordCount > 2) {
				userInput = command[1] + " " + command[2];
			}
			
			//full website
			String weatherURL = myUrlCity + userInput + myKey;
			
			//create weather object 
			URL url = new URL(weatherURL); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result = read.readLine();
			
			// parse api info
			String message = parseJSON(result);
			
			//send temperature to user
			sendMessage(channel, sender + message);
    	}
	}

    // parse the JSON
	public String parseJSON(String json) {
		//creates json object
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		JsonObject main = object.getAsJsonObject("main");
		
		//temp holds the temperature in kelvin
		double temp = main.get("temp").getAsDouble();
		double tempMin = main.get("temp_min").getAsDouble();
		double tempMax = main.get("temp_max").getAsDouble();
		
		//convert kelvin into fahrenheit
		tempMin = (9.0 / 5.0) * (tempMin - 273.0) + 32.0;
		tempMax = (9.0 / 5.0) * (tempMax - 273.0) + 32.0;
		temp = (9.0 / 5.0) * (temp - 273.0) + 32.0;
		
		//round decimal to 2 positions
		BigDecimal b1 = new BigDecimal(temp);
	    MathContext m = new MathContext(4); // 4 precision 
	    // b1 is rounded using m 
	    BigDecimal temp2D = b1.round(m); 
	    
	    BigDecimal b2 = new BigDecimal(tempMin);
	    // b2 is rounded using m 
	    BigDecimal tempMin2D = b2.round(m); 
	    
	    BigDecimal b3 = new BigDecimal(tempMax);
	    // b3 is rounded using m 
	    BigDecimal tempMax2D = b3.round(m); 
	    
	    String finalTemp = temp2D.toString();
		
		//return temperature as a string		
		return (": The weather's going to be " + finalTemp + " degrees fahrenheit " + "with a high of " + tempMax2D + 
				" degrees fahrenheit " + "and a low of " + tempMin2D + " degrees fahrenheit ");
	}
}