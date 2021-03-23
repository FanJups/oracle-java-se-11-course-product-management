/**
 * 
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author oracle
 *
 */
public final class Food extends Product {
	
	private LocalDate bestBefore;
	
	

	/**
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 * @param bestBefore
	 */
	 Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;
	}
	

	/**
	 * Get the value of the best before date for the product
	 * 
	 * @return the bestBefore
	 */
	@Override
	public LocalDate getBestBefore() {
		return bestBefore;
	}
	
	
	

	@Override
	public BigDecimal getDiscount() {
		
		return bestBefore.equals(LocalDate.now()) ? super.getDiscount() : BigDecimal.ZERO;
	}


	@Override
	public String toString() {
		return  super.toString() + ", " + bestBefore ;
	}

	
	@Override
	public Product applyRating(Rating newRating) {
		
		return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
	}


}
