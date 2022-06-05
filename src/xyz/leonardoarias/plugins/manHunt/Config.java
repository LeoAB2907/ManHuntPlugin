package xyz.leonardoarias.plugins.manHunt;

/**
 * Implements an easy way to access the config file
 * 
 * @author leoab2907
 */
public class Config {

	private boolean instaKill;
	private boolean freeze;
	private boolean compassTracking;
	private boolean giveCompass;
	private boolean compassRandomInDifferentWorlds;
	private boolean runnerInvisibleNameTag;
	private boolean drawLine;

	public Config() {
		reload();
	}

	/**
	 * Reload config to defaults
	 */
	public void reload() {
		instaKill = false;
		compassTracking = true;
		giveCompass = true;
		freeze = false;
		compassRandomInDifferentWorlds = true;
		runnerInvisibleNameTag = false;
		drawLine = false;
	}

	/**
	 * @return true if a compass draws a line to runner
	 */
	public boolean isDrawLine() {
		return drawLine;
	}

	/**
	 * @return true is runner name tag is invisible
	 */
	public boolean isRunnerInvisibleNameTag() {
		return runnerInvisibleNameTag;
	}

	/**
	 * @return true if hunter can one shot the runner
	 */
	public boolean isInstaKill() {
		return instaKill;
	}

	/**
	 * @return true if runner freezes hunter when seeing it
	 */
	public boolean freeze() {
		return freeze;
	}

	/**
	 * @return true if compass tracking is enabled
	 */
	public boolean isCompassTracking() {
		return compassTracking;
	}

	/**
	 * @return true if compass is random in different worlds
	 */
	public boolean isCompassRandomInDifferentWorlds() {
		return compassRandomInDifferentWorlds;
	}

	/**
	 * @return true if hunter gets compass back after dying
	 */
	public boolean giveCompass() {
		return giveCompass;
	}

	/**
	 * Sets drawLine to b
	 * @param b
	 */
	public void setDrawLine(boolean b) {
		drawLine = b;
	}

	/**
	 * Sets runnerInvisbleNameTag to b
	 * @param b
	 */
	public void setRunnerInvisibleNameTag(boolean b) {
		runnerInvisibleNameTag = b;
	}

	/**
	 * Sets instaKill to b
	 * @param b
	 */
	public void setInstaKill(boolean b) {
		instaKill = b;
	}

	/**
	 * Sets freeze to b
	 * @param b
	 */
	public void setFreeze(boolean b) {
		freeze = b;
	}

	/**
	 * Sets compassTracking to b
	 * @param b
	 */
	public void setCompassTracking(boolean b) {
		compassTracking = b;
	}

	/**
	 * Sets compassRandomInDifferentWorlds to b
	 * @param b
	 */
	public void setCompassRandomInDifferentWorlds(boolean b) {
		compassRandomInDifferentWorlds = b;
	}

	/**
	 * Sets giveCompass to b
	 * @param b
	 */
	public void setGiveCompass(boolean b) {
		giveCompass = b;
	}
}
