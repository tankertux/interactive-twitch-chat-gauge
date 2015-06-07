package net.tankertux;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.User;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class BotRunner {
	private static final Properties properties = new TwitchProperties();

	public static void main(String[] args) throws InterruptedException, NickAlreadyInUseException, IOException, IrcException {
		TwitchBot bot = new TwitchBot(properties.getProperty("bot.name"), properties.getProperty("broadcaster"));
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(37011);

		SocketIOServer server = new SocketIOServer(config);
		server.addListeners(new BotGaugeListenerService(bot));
		server.start();

		bot.setVerbose(true);
		bot.setAutoNickChange(true);

		try {
			bot.connect(properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")), properties.getProperty("password"));
			String channel = "#" + properties.getProperty("broadcaster");
			bot.joinChannel(channel);
			User[] users = bot.getUsers(channel);
			for (User user : users) {
				Logger.getLogger(BotRunner.class).info(String.format("%s is connected", user));
			}
			bot.sendMessage(channel, properties.getProperty("bot.name") + " connected *beep boop*");
			File shutdownSignal = new File(properties.getProperty("shutdown.file"));
			while (!shutdownSignal.exists()) {
				Thread.sleep(30000);
			}
		} finally {
			bot.disconnect();
			server.stop();
		}
	}
}
