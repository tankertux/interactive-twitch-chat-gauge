package net.tankertux;

import java.io.IOException;
import java.util.Properties;

public class TwitchProperties extends Properties{
	private static final long serialVersionUID = 4033468401444758413L;

	public TwitchProperties() {
		try {
			load(this.getClass().getClassLoader().getResourceAsStream("twitch.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load twitch.properties from the classpath", e);
		}
	}
}
