

public class MyBotMain {
	
	public static void main(String[] args) throws Exception {
		// create the bots
		MyBotWeather bot = new MyBotWeather();
		MyBotRon bot2 = new MyBotRon();
		
		// debugging
		bot.setVerbose(true);
		bot2.setVerbose(true);
		
		// connect the bots to freenode
		bot.connect("irc.freenode.net");
		bot2.connect("irc.freenode.net");
		
		// bots join the #wasifBot channel.
		bot.joinChannel("#wasifBot");
		bot2.joinChannel("#wasifBot");
		
		// send intro message with instructions
		bot.sendMessage("#wasifBot", "Hey! Enter \"quote\" to see a quote from Ron Swanson or "
			+ "enter \"weather [cityname]\" or \"weather [zipcode]\" to see the weather");
    }
}
