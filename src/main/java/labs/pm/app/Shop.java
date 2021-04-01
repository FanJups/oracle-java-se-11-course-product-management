/**
 *
 */
package labs.pm.app;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		ProductManager pm = ProductManager.getInstance();

		AtomicInteger clientCount = new AtomicInteger(0);

		Callable<String> client = () -> {

			String clientId = "Client " + clientCount.incrementAndGet();

			String threadName = Thread.currentThread().getName();

			// nextInt(x) generates a value between 0 and x, x is not included
			// 0 <= value < x
			// 0 <= value <= x-1
			/*
			 * nextInt(5) instead of nextInt(63) because I was forced to generate 5 product
			 * files by myself I didn't have access to these files used by the instructor
			 */

			int productId = ThreadLocalRandom.current().nextInt(5) + 101;

			// nextInt(4) because the Map contains 4 key-value pairs

			String languageTag = ProductManager.getSupportedLocales().stream()
					.skip(ThreadLocalRandom.current().nextInt(4)).findFirst().get();

			StringBuilder log = new StringBuilder();

			log.append(clientId + " " + threadName + "\n-\tstart of log \t-\n");

			log.append(pm.getDiscounts(languageTag).entrySet().stream()
					.map(entry -> entry.getKey() + "\t" + entry.getValue()).collect(Collectors.joining("\n")));

			Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");

			log.append((product != null) ? ("\nProduct " + productId + "reviewed\n")
					: ("\nProduct " + productId + "not reviewed\n"));

			pm.printProductReport(productId, languageTag, clientId);

			log.append(false);

			log.append("\n-\tend of log \t-\n");

			return log.toString();

		};

		List<Callable<String>> clients = Stream.generate(() -> client).limit(5).collect(Collectors.toList());

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		try {
			List<Future<String>> results = executorService.invokeAll(clients);

			executorService.shutdown();

			results.stream().forEach(result -> {

				try {
					System.out.println(result.get());
				} catch (InterruptedException | ExecutionException e) {
					Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error retrieving client log", e);
				}
			});
		} catch (InterruptedException e) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients", e);
		}

		/*
		 * pm.createProduct(164, "Kombucha", BigDecimal.valueOf(1.99),
		 * Rating.NOT_RATED); pm.reviewProduct(164, Rating.TWO_STAR,
		 * "Looks like tea but is it ?"); pm.reviewProduct(164, Rating.FOUR_STAR,
		 * "Fine tea"); pm.reviewProduct(164, Rating.FOUR_STAR, "This is not tea");
		 * pm.reviewProduct(164, Rating.FIVE_STAR, "Perfect!");
		 */
		// pm.printProductReport(164);

		// pm.dumpData();

		// pm.restoreData();

		// pm.printProductReport(105);

		// pm.printProductReport(164);

		// pm.createProduct(101,"Tea",BigDecimal.valueOf(1.99),Rating.NOT_RATED);

		// pm.parseProduct("D,101,Tea,1.99,0,2021-09-19");

		// pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");

		/*
		 * pm.parseReview("101,4,Nice hot cup of tea");
		 * pm.parseReview("101,2,Rather weak tea"); pm.parseReview("101,4,Good tea");
		 * pm.parseReview("101,4,Fine tea");
		 *
		 * pm.parseReview("101,2,Perfect tea");
		 *
		 * pm.parseReview("101,3,Just add some lemon");
		 */

		/*
		 * pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
		 * pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea"); pm.reviewProduct(101,
		 * Rating.FOUR_STAR, "Fine tea"); pm.reviewProduct(101, Rating.FIVE_STAR,
		 * "Perfect tea"); pm.reviewProduct(101, Rating.THREE_STAR,
		 * "Just add some lemon");
		 */
		// pm.printProductReport(101);

		// pm.parseProduct("F,103,Cake,3.99,0,2021-09-19");
		// pm.printProductReport(103);

		// pm.changeLocale("fr-FR");

		/*
		 * pm.createProduct(102,"Coffee",BigDecimal.valueOf(1.99),Rating.NOT_RATED);
		 * pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
		 * pm.reviewProduct(102, Rating.ONE_STAR, "Where is the milk?!");
		 * pm.reviewProduct(102, Rating.FIVE_STAR,
		 * "It's perfect with ten spoons of sugar!");
		 */
		// pm.printProductReport(102);

		/*
		 * pm.createProduct(103,"Cake",BigDecimal.valueOf(3.99),Rating.NOT_RATED,
		 * LocalDate.now().plusDays(2));
		 *
		 *
		 * pm.reviewProduct(103, Rating.FIVE_STAR, "Very nice cake");
		 * pm.reviewProduct(103, Rating.FOUR_STAR,
		 * "It good, but I've expected more chocolate"); pm.reviewProduct(103,
		 * Rating.FIVE_STAR, "This cake is perfect!");
		 *
		 * //pm.printProductReport(103);
		 *
		 * pm.createProduct(104,"Cookie",BigDecimal.valueOf(2.99),Rating.NOT_RATED,
		 * LocalDate.now());
		 *
		 * pm.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
		 * pm.reviewProduct(104, Rating.THREE_STAR, "OK");
		 *
		 * //pm.printProductReport(104);
		 *
		 * pm.createProduct(105,"Hot Chocolate",BigDecimal.valueOf(2.50),Rating.
		 * NOT_RATED);
		 *
		 * pm.reviewProduct(105, Rating.FOUR_STAR, "Tasty!"); pm.reviewProduct(105,
		 * Rating.FOUR_STAR, "Not bad at all");
		 */
		// pm.printProductReport(105);

		/*
		 * pm.createProduct(106,"Chocolate",BigDecimal.valueOf(2.50),Rating.NOT_RATED,
		 * LocalDate.now().plusDays(3));
		 *
		 * pm.reviewProduct(106, Rating.TWO_STAR, "Too sweet"); pm.reviewProduct(106,
		 * Rating.THREE_STAR, "Better then cookie"); pm.reviewProduct(106,
		 * Rating.TWO_STAR, "Too bitter"); pm.reviewProduct(106, Rating.ONE_STAR,
		 * "I don't get it!");
		 *
		 * pm.printProductReport(106);
		 */
		// Sort by rating descending order

		// Comparator<Product> ratingSorter = (p1,p2) -> p2.getRating().ordinal() -
		// p1.getRating().ordinal();

		// Sort by price descending order

		// Comparator<Product> priceSorter = (p1,p2) ->
		// p2.getPrice().compareTo(p1.getPrice());

		// Predicate<Product> filter = p -> p.getPrice().floatValue() < 2;

		// pm.printProducts(filter,ratingSorter);

		// pm.printProducts(priceSorter);

		// pm.printProducts(ratingSorter.thenComparing(priceSorter));

		// pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());

		// pm.getDiscounts().forEach((rating,discount) ->
		// System.out.println(rating+"\t"+discount));

	}

}
