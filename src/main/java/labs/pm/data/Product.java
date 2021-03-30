/*
 * 
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * {@code Product} class represents properties and behaviours of
 * product objects in the Product Management System
 * <br>
 * Each product has an id, name and price
 * <br>
 * Each product can have a discount calculated based on a
 * {@link DISCOUNT_RATE discount rate}.
 *
 * @author oracle
 * @version 4.0
 */


public abstract class Product implements Rateable<Product>,Serializable {
	
	/**
	 * A constant that defines a 
	 * {@link java.math.BigDecimal BigDecimal} value
	 * of the discount rate
	 * <br>
	 * Discount rate is 10%
	 */

	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	
	/** The id. */
	private int id;
	
	/** The name. */
	private String name;
	
	/** The price. */
	private BigDecimal price;
	
	/** The rating. */
	private Rating rating;
	
	 Product() {	
		this(0,"no name",BigDecimal.ZERO);	
	}
	
	/**
	 * @param id
	 * @param name
	 * @param price
	 * @param rating
	 */
	 Product(int id, String name, BigDecimal price, Rating rating) {
		
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}
	
	

	/**
	 * @param id
	 * @param name
	 * @param price
	 */
	 Product(int id, String name, BigDecimal price) {
		
		this(id,name,price,Rating.NOT_RATED);
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	
	
	/**
	 * Calculates discount based on a product price and
	 * {@link DISCOUNT_RATE discount rate}.
	 *
	 * @return a {@link java.math.BigDecimal BigDecimal} value of the discount
	 */

	
	public BigDecimal getDiscount()
	{
		return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
	}

	/**
	 * @return the rating
	 */
	@Override
	public Rating getRating() {
		return rating;
	}
	
	public abstract Product applyRating(Rating newRating);
	
	/**
	 * Get the value of the best before date for the product
	 * 
	 */
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}
	
	

	@Override
	public String toString() {
		return id + ", " + name + ", " + price + ", " + getDiscount() + ", " + rating.getStars()+ ", " + getBestBefore();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		// obj !=null && getClass() == obj.getClass()
		
		if(obj instanceof Product) {
			
			final Product other = (Product) obj;
			
			return this.id == other.id ;
		}
		
		return false;
	}

	

}
