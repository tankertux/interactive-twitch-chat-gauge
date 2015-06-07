package net.tankertux;

import org.apache.log4j.Logger;
import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {

	private Logger logger;
	private String admin;
	private Gauge gauge;

	public TwitchBot(String nick, String admin) {
		this.setAdmin(admin);
		this.setName(nick);
		this.logger = Logger.getLogger(getClass());
	}

	private void setAdmin(String admin) {
		this.admin = admin;
	}

	@Override
	protected void onConnect() {
		logger.info("Connected!");
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(message.startsWith(Gauge.COMMAND) && admin.equals(sender)){
				gauge = Gauge.fromCommand(message);
		}
		if(gauge.matches(message)) {
			gauge.increment();
		}
		if(gauge.isActivated()){
			sendMessage(channel, gauge.getResponse());
		}
	}

	public Gauge getGauge() {
		return this.gauge;
	}
}
