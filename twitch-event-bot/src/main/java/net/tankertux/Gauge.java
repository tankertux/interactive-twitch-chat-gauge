package net.tankertux;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;

public class Gauge {

	public static final String COMMAND = "!gauge";
	private static final Pattern COMMAND_PATTERN = Pattern.compile(COMMAND + "\\s*(\\d+)\\s*\"([^\"]+)\"\\s*\"([^\"]+)\"\\s*");

	public static Gauge fromCommand(String command) {
		Matcher matcher = COMMAND_PATTERN.matcher(command);
		Gauge gauge = new Gauge();
		if (matcher.matches()) {
			gauge.setThreshold(Integer.parseInt(matcher.group(1)));
			gauge.setKeyphrase(matcher.group(2));
			gauge.setResponse(matcher.group(3));
			Logger.getLogger(Gauge.class).debug("Created " + gauge.toString());
			return gauge;
		} else {
			Logger.getLogger(Gauge.class).warn("Invalid command: (e.g. !gauge 100 \"hype\" \"Hype achieved!\")");
			Logger.getLogger(Gauge.class).warn("Actual input   : " + command);
			gauge.setInvocations(Integer.MIN_VALUE);
			gauge.setThreshold(Integer.MAX_VALUE);
			return gauge;
		}
	}

	private String response;
	private String keyphrase;
	private int threshold;
	private int invocations = -1;
	private Logger logger;

	public Gauge() {
		this.setInvocations(0);
		this.setResponse("Oh man, that wasn't supposed to happen. Look at this place! :<");
		this.setKeyphrase("barf");
		this.logger = Logger.getLogger(getClass());
	}

	private void setInvocations(int i) {
		this.invocations = 0;
	}

	public String getResponse() {
		return response;
	}

	public String getKeyphrase() {
		return keyphrase;
	}

	public int getThreshold() {
		return threshold;
	}

	private void setResponse(String group) {
		this.response = group;
	}

	private void setKeyphrase(String group) {
		this.keyphrase = group;
	}

	private void setThreshold(int parseInt) {
		this.threshold = parseInt;
	}

	public boolean matches(String message) {
		boolean matches = message.equals(getKeyphrase());
		if (!matches)
			logger.debug(String.format("Message <%s> did not match keyphrase <%s>", message, getKeyphrase()));
		return matches;
	}

	public void increment() {
		logger.debug("Incremented");
		this.invocations++;
	}

	public boolean isActivated() {
		boolean activated = this.getInvocations() >= this.getThreshold();
		if(activated) logger.debug("GAUGE ACTIVATED");
		else logger.debug(String.format("Sorry, %s less than threshold %s", this.getInvocations(), this.getThreshold()));
		return activated;
	}

	public int getInvocations() {
		return invocations;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("threshold", getThreshold()).append("keyphrase", getKeyphrase()).append("response", getResponse()).toString();
	}


}
