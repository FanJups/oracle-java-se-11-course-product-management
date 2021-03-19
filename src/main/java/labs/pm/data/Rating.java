/**
 * 
 */
package labs.pm.data;

/**
 * @author oracle
 *
 */
public enum Rating {
	
	NOT_RATED("\u2606\u2606\u2606\u2606\u2606"),
	ONE_STAR("\u2605\u2606\u2606\u2606\u2606"),
	TWO_STAR("\u2605\u2605\u2606\u2606\u2606"),
	THREE_STAR("\u2605\u2605\u2605\u2606\u2606"),
	FOUR_STAR("\u2605\u2605\u2605\u2605\u2606"),
	FIVE_STAR("\u2605\u2605\u2605\u2605\u2605");
	
	private String stars;

	/**
	 * @param stars
	 */
	private Rating(String stars) {
		this.stars = stars;
	}

	/**
	 * @return the stars
	 */
	public String getStars() {
		return stars;
	}
	
	

}
