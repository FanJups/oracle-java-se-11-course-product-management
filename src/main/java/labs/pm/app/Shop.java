/**
 * 
 */
package labs.pm.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import labs.pm.data.Drink;
import labs.pm.data.Food;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;


/**
 * {@code Shop} class represents an application that manages Products.
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ProductManager pm = new ProductManager(Locale.US);
		
		
		
		Product p1 = pm.createProduct(101,"Tea",BigDecimal.valueOf(1.99),Rating.NOT_RATED);
		
		pm.printProductReport();
		
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
		p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
		p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
		p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
		
		pm.printProductReport();

		/*
		 * Product p2 =
		 * pm.createProduct(102,"Coffee",BigDecimal.valueOf(1.99),Rating.FOUR_STAR);
		 * Product p3 =
		 * pm.createProduct(103,"Cake",BigDecimal.valueOf(3.99),Rating.FIVE_STAR,
		 * LocalDate.now().plusDays(2)); Product p4 =
		 * pm.createProduct(103,"Cookie",BigDecimal.valueOf(3.99),Rating.TWO_STAR,
		 * LocalDate.now()); Product p5 = p3.applyRating(Rating.THREE_STAR); Product p6
		 * =pm.createProduct(104,"Chocolate",BigDecimal.valueOf(2.99),Rating.FIVE_STAR);
		 * Product p7
		 * =pm.createProduct(104,"Chocolate",BigDecimal.valueOf(2.99),Rating.FIVE_STAR,
		 * LocalDate.now().plusDays(2)); Product p8 = p4.applyRating(Rating.FIVE_STAR);
		 * Product p9 = p1.applyRating(Rating.TWO_STAR);
		 * System.out.println(p6.equals(p7));
		 * 
		 * System.out.println(p1); System.out.println(p2); System.out.println(p3);
		 * System.out.println(p4); System.out.println(p5); System.out.println(p6);
		 * System.out.println(p7); System.out.println(p8); System.out.println(p9);
		 * 
		 */	
		
	
	}

}
