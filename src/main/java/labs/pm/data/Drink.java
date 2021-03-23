/**
 * 
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @author oracle
 *
 */
public final class Drink extends Product {

	/**
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 */
	 Drink(int id, String name, BigDecimal price, Rating rating) {
		super(id, name, price, rating);
		
	}
	
	@Override
	public BigDecimal getDiscount() {
		
		LocalTime now = LocalTime.now();
		
		return (now.isAfter(LocalTime.of(16, 30)) && now.isBefore(LocalTime.of(17, 30))) ? super.getDiscount() : BigDecimal.ZERO;
	}

	@Override
	public Product applyRating(Rating newRating) {
		
		return new Drink(getId(), getName(), getPrice(), newRating);
	}

	
	

}
