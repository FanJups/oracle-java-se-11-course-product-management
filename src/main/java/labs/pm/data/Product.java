/*
 * 
 */
package labs.pm.data;

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;

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
 * @author Fanon JUPKWO
 * @version 4.0
 */


public class Product {
	
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
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId( final int id) {
		this.id = id;
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
	 * Sets the name.
	 *
	 * @param name the name to set
	 */
	public void setName( final String name) {
		this.name = name;
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
	 * Sets the price.
	 *
	 * @param price the price to set
	 */
	public void setPrice( final BigDecimal price) {
		this.price = price;
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
	
	

}
