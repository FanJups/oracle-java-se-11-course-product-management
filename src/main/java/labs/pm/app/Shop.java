/**
 * 
 */
package labs.pm.app;

import java.math.BigDecimal;

import labs.pm.data.Product;


/**
 * {@code Shop} class represents an application that manages Products.
 *
 * @author Fanon JUPKWO
 * @version 4.0
 */
public class Shop {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Product p1 = new Product();
		p1.setId(101);
		p1.setName("Tea");
		p1.setPrice(BigDecimal.valueOf(1.99));
		
		System.out.println(p1.getId()+" "+p1.getName()+" "+p1.getPrice()+" "+p1.getDiscount());

	}

}
