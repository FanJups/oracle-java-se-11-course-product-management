/*
 *
 */
package labs.pm.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author oracle
 *
 */

public class ProductManager {

	private Map<Product, List<Review>> products = new HashMap<>();

	// private ResourceFormatter formatter;

	private final ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");

	private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));

	private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

	private final Path reportsFolder = Path.of(config.getString("reports.folder"));

	private final Path dataFolder = Path.of(config.getString("data.folder"));

	private final Path tmpFolder = Path.of(config.getString("tmp.folder"));

	// The instructor uses russian language but I choose to use french

	private static Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
			"en-US", new ResourceFormatter(Locale.US), "fr-FR", new ResourceFormatter(Locale.FRANCE), "zh-CN",
			new ResourceFormatter(Locale.CHINA));

	private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

	private static final ProductManager pm = new ProductManager();

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private Lock writeLock = lock.writeLock();

	private Lock readLock = lock.readLock();

	public static ProductManager getInstance() {
		return pm;
	}

	private ProductManager() {

		loadAllData();

	}

	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {

		Product product = null;

		try {
			writeLock.lock();

			product = new Food(id, name, price, rating, bestBefore);

			products.putIfAbsent(product, new ArrayList<>());

		} catch (Exception e) {

			logger.log(Level.INFO, "Error adding product " + e.getMessage());

			return null;

		} finally {

			writeLock.unlock();
		}

		return product;
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {

		Product product = null;

		try {
			writeLock.lock();

			product = new Drink(id, name, price, rating);

			products.putIfAbsent(product, new ArrayList<>());

		} catch (Exception e) {

			logger.log(Level.INFO, "Error adding product " + e.getMessage());

			return null;

		} finally {

			writeLock.unlock();
		}

		return product;
	}

	public Product reviewProduct(int id, Rating rating, String comments) {

		try {
			writeLock.lock();

			return reviewProduct(findProduct(id), rating, comments);

		} catch (ProductManagerException e) {

			logger.log(Level.INFO, e.getMessage());
			return null;

		} finally {

			writeLock.unlock();

		}

	}

	private Product reviewProduct(Product product, Rating rating, String comments) {

		List<Review> reviews = products.get(product);
		products.remove(product, reviews);

		reviews.add(new Review(rating, comments));

		product = product.applyRating(Rateable.convert(
				(int) Math.round(reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))));

		products.put(product, reviews);

		return product;

	}

	public Product findProduct(int id) throws ProductManagerException {

		try {

			readLock.lock();

			return products.keySet().stream().filter(p -> p.getId() == id).findFirst()
					.orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));

		} finally {

			readLock.unlock();

		}

	}

	public void printProductReport(int id, String languageTag, String client) {
		try {
			readLock.lock();

			printProductReport(findProduct(id), languageTag, client);

		} catch (ProductManagerException e) {

			logger.log(Level.INFO, e.getMessage());

		} catch (IOException e) {

			logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
		} finally {

			readLock.unlock();
		}
	}

	private void printProductReport(Product product, String languageTag, String client) throws IOException {

		ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

		List<Review> reviews = products.get(product);

		Collections.sort(reviews);

		Path productFile = reportsFolder
				.resolve(MessageFormat.format(config.getString("report.file"), product.getId(), client));

		try (PrintWriter out = new PrintWriter(
				new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {

			out.append(formatter.formatProduct(product) + System.lineSeparator());

			if (reviews.isEmpty()) {

				out.append(formatter.getText("no.reviews") + System.lineSeparator());

			} else {

				out.append(reviews.stream().map(r -> formatter.formatReview(r) + System.lineSeparator())
						.collect(Collectors.joining()));

			}

			// System.out.println(txt);

		}

	}

	public void printProducts(Predicate<Product> filter, Comparator<Product> sorter, String languageTag) {

		try {

			readLock.lock();

			ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

			StringBuilder txt = new StringBuilder();

			txt.append(products.keySet().stream().sorted(sorter).filter(filter)
					.map(p -> formatter.formatProduct(p) + "\n").collect(Collectors.joining()));

			System.out.println(txt);

		} finally {

			readLock.unlock();

		}

	}

	private void dumpData() {
		try {

			if (Files.notExists(tmpFolder)) {
				Files.createDirectory(tmpFolder);
			}

			/*
			 * WINDOWS CASE
			 *
			 * In order to avoid java.nio.file.InvalidPathException we replace ':' by '_'
			 *
			 * Exception in thread "main" java.nio.file.InvalidPathException: Illegal char
			 * <:> at index 13: 2021-03-30T07:09:42.310850100Z.tmp at
			 * java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
			 * at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
			 * at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77) at
			 * java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92) at
			 * java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:229) at
			 * java.base/java.nio.file.Path.resolve(Path.java:515) at
			 * labs.pm.data.ProductManager.dumpData(ProductManager.java:242) at
			 * labs.pm.app.Shop.main(Shop.java:48)
			 *
			 */

			Path tempFile = tmpFolder
					.resolve(MessageFormat.format(config.getString("temp.file"), Instant.now()).replace(':', '_'));

			try (ObjectOutputStream out = new ObjectOutputStream(
					Files.newOutputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
				out.writeObject(products);

				// products = new HashMap<>();
			}
		} catch (IOException e) {

			logger.log(Level.SEVERE, "Error dumping data " + e.getMessage(), e);

		}
	}

	@SuppressWarnings("unchecked")
	private void restoreData() {
		try {
			Path tempFile = Files.list(tmpFolder).filter(path -> path.getFileName().toString().endsWith("tmp"))
					.findFirst().orElseThrow();

			try (ObjectInputStream in = new ObjectInputStream(
					Files.newInputStream(tempFile, StandardOpenOption.READ))) {
				products = (HashMap) in.readObject();
			}

		} catch (Exception e) {

			logger.log(Level.SEVERE, "Error restoring data " + e.getMessage(), e);

		}
	}

	private void loadAllData() {
		try {
			products = Files.list(dataFolder).filter(file -> file.getFileName().toString().startsWith("product"))
					.map(file -> loadProduct(file)).filter(product -> product != null)
					.collect(Collectors.toMap(product -> product, product -> loadReviews(product)));
		} catch (IOException e) {

			logger.log(Level.SEVERE, "Error loading data " + e.getMessage(), e);
		}
	}

	private Product loadProduct(Path file) {
		Product product = null;

		try {
			product = parseProduct(
					Files.lines(dataFolder.resolve(file), Charset.forName("UTF-8")).findFirst().orElseThrow());

		} catch (Exception e) {

			logger.log(Level.WARNING, "Error loading product " + e.getMessage());
		}

		return product;
	}

	private List<Review> loadReviews(Product product) {
		List<Review> reviews = null;

		Path file = dataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));

		if (Files.notExists(file)) {
			reviews = new ArrayList<>();

		} else {

			try {
				reviews = Files.lines(file, Charset.forName("UTF-8")).map(text -> parseReview(text))
						.filter(review -> review != null).collect(Collectors.toList());
			} catch (IOException e) {

				logger.log(Level.WARNING, "Error loading reviews " + e.getMessage());
			}

		}

		return reviews;

	}

	private Review parseReview(String text) {
		Review review = null;

		try {
			Object[] values = reviewFormat.parse(text);

			review = new Review(Rateable.convert(Integer.parseInt((String) values[0])), (String) values[1]);

		} catch (ParseException | NumberFormatException e) {

			logger.log(Level.WARNING, "Error parsing review " + text);
		}

		return review;
	}

	private Product parseProduct(String text) {
		Product product = null;

		try {
			Object[] values = productFormat.parse(text);

			int id = Integer.parseInt((String) values[1]);
			String name = (String) values[2];
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
			Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));

			switch ((String) values[0]) {

			case "D":

				product = new Drink(id, name, price, rating);
				break;

			case "F":

				LocalDate bestBefore = LocalDate.parse((String) values[5]);
				product = new Food(id, name, price, rating, bestBefore);

			}

		} catch (ParseException | NumberFormatException | DateTimeParseException e) {

			logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage());
		}

		return product;
	}

	public Map<String, String> getDiscounts(String languageTag) {

		try {

			readLock.lock();

			ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));

			return products.keySet().stream()
					.collect(Collectors.groupingBy(product -> product.getRating().getStars(),
							Collectors.collectingAndThen(
									Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
									discount -> formatter.moneyFormat.format(discount))));

		} finally {

			readLock.unlock();
		}

	}

	private static class ResourceFormatter {
		private Locale locale;
		private ResourceBundle resources;
		private DateTimeFormatter dateFormat;
		private NumberFormat moneyFormat;

		/**
		 * @param locale
		 */
		private ResourceFormatter(Locale locale) {
			/**
			 * Problem: <br>
			 * After creating resources_fr_FR.properties, my program was always using
			 * content coming <br>
			 * from this file even if I wanted to change the locale. Why ? The default
			 * language of my computer is French. <br>
			 * Solution: <br>
			 * Set Default locale <br>
			 * Useful links: <br>
			 *
			 * @see <a href=
			 *      "https://stackoverflow.com/questions/24305512/how-to-get-the-default-resourcebundle-regardless-of-current-default-locale">You
			 *      can pass in a ResourceBundle.Control which, regardless of requested
			 *      Locale, always searches only the root ResourceBundle</a>
			 *
			 *      <br>
			 * @see <a href=
			 *      "https://stackoverflow.com/questions/8809098/how-do-i-set-the-default-locale-in-the-jvm">how-do-i-set-the-default-locale-in-the-jvm</a>
			 */

			Locale.setDefault(Locale.UK);

			this.locale = locale;

			resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);

			dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);

			moneyFormat = NumberFormat.getCurrencyInstance(locale);

		}

		private String formatProduct(Product product) {
			return MessageFormat.format(resources.getString("product"), product.getName(),
					moneyFormat.format(product.getPrice()), product.getRating().getStars(),
					dateFormat.format(product.getBestBefore()));
		}

		private String formatReview(Review review) {
			return MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
					review.getComments());
		}

		private String getText(String key) {
			return resources.getString(key);
		}

	}

}
