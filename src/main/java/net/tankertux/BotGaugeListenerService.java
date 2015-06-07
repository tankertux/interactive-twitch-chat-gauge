package net.tankertux;

import org.apache.log4j.Logger;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnEvent;

public class BotGaugeListenerService {
	private TwitchBot bot;
	private Logger logger;

	public BotGaugeListenerService(TwitchBot bot) {
		this.logger = Logger.getLogger(getClass());
		this.setBot(bot);
	}
	
	@OnConnect
	public void onConnect(SocketIOClient client) {
		logger.debug("Connect event detected");
		client.sendEvent("initialize", this.getBot().getGauge());
		logger.debug("Socket Initialized");
	}
	
	@OnEvent("refresh")
	public void onRefresh(SocketIOClient client){
		logger.debug("Refresh event detected");
		client.sendEvent("report", this.getBot().getGauge());
		logger.debug("Socket refreshed");
	}

	public TwitchBot getBot() {
		return bot;
	}

	private void setBot(TwitchBot bot) {
		this.bot = bot;
	}
	
}
