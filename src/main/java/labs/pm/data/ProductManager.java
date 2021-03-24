package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author oracle
 *
 */

public class ProductManager {
	
	private Map<Product,List<Review>> products = new HashMap<>();
	
	private ResourceFormatter formatter;
	
	private static Map<String, ResourceFormatter> formatters = Map.of(
			"en-GB", new ResourceFormatter(Locale.UK),
			"en-US", new ResourceFormatter(Locale.US),
			"fr-FR", new ResourceFormatter(Locale.FRANCE),
			"ru-RU", new ResourceFormatter(new Locale("ru","RU")),
			"zh-CN", new ResourceFormatter(Locale.CHINA));
	
	
	
	/**
	 * @param locale
	 */
	public ProductManager(Locale locale) {
		
		this(locale.toLanguageTag());
		
	}
	
	public ProductManager(String languageTag) {
		
		changeLocale(languageTag);
		
	}

	
	public void changeLocale(String languageTag)
	{
		formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
	}
	
	public static Set<String> getSupportedLocales()
	{
		return formatters.keySet();
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		
		Product product = new Food(id, name, price, rating, bestBefore);
		
		products.putIfAbsent(product, new ArrayList<>());
		
		return product;
	}
	
	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		
		Product product = new Drink(id, name, price, rating);
		
		products.putIfAbsent(product, new ArrayList<>());
		
		return product;
	}
	
	
	public Product reviewProduct(int id,Rating rating,String comments) {
		
		return reviewProduct(findProduct(id),rating,comments);
		
	}
	
	
	public Product reviewProduct(Product product,Rating rating,String comments)
	{
		
		List<Review> reviews = products.get(product);	
		products.remove(product, reviews);
		
		reviews.add(new Review(rating,comments));
		
		
		int sum = 0;
		
		for(Review review : reviews)
		{
			sum += review.getRating().ordinal();
		}
		
		
		
		product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
		
		products.put(product, reviews);
		
		return product;
		
	}
	
	public Product findProduct(int id)
	{
		Product  result =null;
		
		for(Product product : products.keySet())
		{
			if(product.getId() == id) {
				
				result = product;
				break;
			}
			
		}
		
		return result;
		
	}
	
	public void printProductReport(int id)
	{
		printProductReport(findProduct(id));
	}

	public void printProductReport(Product product)
	{
		
		List<Review> reviews = products.get(product);
		StringBuilder txt = new StringBuilder();
		
		txt.append(formatter.formatProduct(product));
		
		txt.append("\n");
		
		Collections.sort(reviews);
		
		for(Review review : reviews) {
			
		
			txt.append(formatter.formatReview(review));
			
			txt.append("\n");
			
		}
		
		if(reviews.isEmpty())
		{
			txt.append(formatter.getText("no.reviews"));
			
			txt.append("\n");

		}
				

		System.out.println(txt);
	}
	
	public void printProducts(Comparator<Product> sorter)
	{
		List<Product> productList = new ArrayList<>(products.keySet());
		
		productList.sort(sorter);
		
		StringBuilder txt = new StringBuilder();
		
		for(Product product : productList)
		{
			txt.append(formatter.formatProduct(product));
			
			txt.append("\n");

		}
		
		System.out.println(txt);
	}
	
	private static class ResourceFormatter
	{
		private Locale locale;
		private ResourceBundle resources;
		private DateTimeFormatter dateFormat;
		private NumberFormat moneyFormat;
		
		/**
		 * @param locale
		 */
		private ResourceFormatter(Locale locale)
		{
			/**
			 * Problem:
			 * <br>
			 * After creating resources_fr_FR.properties, my program was always using content coming
			 * <br>
			 * from this file even if I wanted to change the locale.
			 * Why ?
			 * The default language of my computer is French.
			 * <br>
			 * Solution:
			 * <br>
			 * Set Default locale
			 * <br>
			 * Useful links:
			 * <br>
			 * @see <a href= "https://stackoverflow.com/questions/24305512/how-to-get-the-default-resourcebundle-regardless-of-current-default-locale">You can pass in a ResourceBundle.Control which, regardless of requested Locale, always searches only the root ResourceBundle</a>
 *      
 *     		<br>
 *        @see <a href= "https://stackoverflow.com/questions/8809098/how-do-i-set-the-default-locale-in-the-jvm">how-do-i-set-the-default-locale-in-the-jvm</a>
			 * */
			
			Locale.setDefault(Locale.UK);
			
			this.locale = locale;
						
			resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
			
			dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
			
			moneyFormat = NumberFormat.getCurrencyInstance(locale);

		}
		
		private String formatProduct(Product product)
		{
			return MessageFormat.format(resources.getString("product"),
					product.getName(),
					moneyFormat.format(product.getPrice()),
					product.getRating().getStars(),
					dateFormat.format(product.getBestBefore()));
		}
		
		private String formatReview(Review review)
		{
			return MessageFormat.format(resources.getString("review"),
					review.getRating().getStars(),
					review.getComments());
		}
		
		private String getText(String key)
		{
			return resources.getString(key);
		}

		
	}

	
}
