import java.nio.file.*; // Импортируем классы для работы с файлами и путями.
import java.io.IOException; // Для обработки исключений, связанных с операциями ввода-вывода.
import java.util.*; // Импортируем коллекции и утилиты, такие как List, Map и Scanner.
import java.util.logging.*; // Для ведения логов (журналирования).

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName()); 
    // Создаем экземпляр логгера для записи сообщений о работе программы.

    // Константы для имен файлов и отчетов.
    private static final String SALES_FILE = "sales.csv";
    private static final String PRODUCTS_FILE = "products.csv";
    private static final String CUSTOMERS_FILE = "customers.csv";
    private static final String TOTAL_SALES_REPORT = "total_sales_report.txt";
    private static final String POPULAR_PRODUCTS_REPORT = "popular_products_report.txt";
    private static final String UNPOPULAR_PRODUCTS_REPORT = "unpopular_products_report.txt";
    private static final String CUSTOMERS_REPORT = "customers_report.txt";
    private static final String SALES_TRENDS_REPORT = "sales_trends_report.txt";

    public static void main(String[] args) {
        logger.info("Start"); 
        // Логируем сообщение о старте программы.

        try {
            // Загружаем данные о продажах, продуктах и клиентах из файлов.
            List<Sale> sales = loadSales(SALES_FILE);
            List<Product> products = loadProducts(PRODUCTS_FILE);
            List<Customer> customers = loadCustomers(CUSTOMERS_FILE);

            // Инициализируем анализатор данных и генератор отчетов.
            Sales analyzer = new Sales(sales, products, customers);
            ReportGenerator reportGenerator = new ReportGenerator();
            Scanner scanner = new Scanner(System.in); // Сканер для чтения ввода пользователя.

            // Получаем статистику продаж по продуктам.
            Map<Integer, Long> productSalesCount = analyzer.getProductSalesCount();
            boolean running = true; // Флаг для управления циклом программы.

            while (running) { // Основной цикл программы.
                displayMenu(); // Выводим меню на экран.

                int choice = scanner.nextInt(); // Считываем выбор пользователя.
                switch (choice) { // Обрабатываем выбор пользователя.
                    case 1:
                        TotalSales(analyzer, reportGenerator); // Сумма всех продаж.
                        break;
                    case 2:
                        PopularProducts(analyzer, reportGenerator, productSalesCount); // Популярные товары.
                        break;
                    case 3:
                        UnpopularProducts(analyzer, reportGenerator, productSalesCount); // Непопулярные товары.
                        break;
                    case 4:
                        CustomerSpending(analyzer, reportGenerator, scanner); // Топ клиентов.
                        break;
                    case 5:
                        MonthlySalesTrends(analyzer, reportGenerator); // Тенденции продаж.
                        break;
                    case 6:
                        running = false; // Завершаем программу.
                        System.out.println("Закрыть приложение.");
                        break;
                    default:
                        System.out.println("Неверный выбор, пожалуйста, попробуйте снова."); // Обрабатываем неверный ввод.
                }
            }
            scanner.close(); // Закрываем сканер.
            logger.info("Отчеты успешно сформированы."); // Логируем успешное завершение.
        } catch (IOException e) {
            logger.severe("Ошибка загрузки данных: " + e.getMessage()); // Логируем ошибку при работе с файлами.
        }
    }

    private static void displayMenu() { 
        // Выводим меню выбора на экран.
        System.out.println("Выберите вариант:");
        System.out.println("1. Определить текущую сумму всех продаж");
        System.out.println("2. Определить пять самых популярных товаров");
        System.out.println("3. Определить пять самых непопулярных товаров");
        System.out.println("4. Определить покупателей набравших товаров на сумму более заданной");
        System.out.println("5. Определить тенденции продаж");
        System.out.println("6. Выход");
        System.out.print("Введите ваш вариант: ");
    }

    // Методы для обработки каждого пункта меню:
    private static void TotalSales(Sales analyzer, ReportGenerator reportGenerator) {
        double totalSalesAmount = analyzer.getTotalSalesAmount(); // Расчет общей суммы продаж.
        System.out.println("Текущая сумма всех продаж: " + totalSalesAmount); 
        String totalSalesReport = reportGenerator.TotalSalesReport(totalSalesAmount); 
        // Формируем текст отчета.
        reportGenerator.generateReport(TOTAL_SALES_REPORT, "Отчет об общем объеме продаж\n" + totalSalesReport); 
        // Генерируем отчет в файл.
    }

    private static void PopularProducts(Sales analyzer, ReportGenerator reportGenerator, Map<Integer, Long> productSalesCount) {
        List<Product> topProducts = analyzer.getTopProducts(); // Получаем список популярных продуктов.
        String topProductsReport = reportGenerator.PopularProductsReport(topProducts, productSalesCount); 
        // Формируем текст отчета.
        System.out.println(topProductsReport); 
        reportGenerator.generateReport(POPULAR_PRODUCTS_REPORT, "Отчет о пяти самых популярных товарах: \n" + topProductsReport); 
        // Генерируем отчет в файл.
    }

    private static void UnpopularProducts(Sales analyzer, ReportGenerator reportGenerator, Map<Integer, Long> productSalesCount) {
        List<Product> bottomProducts = analyzer.getUnpopularProducts(); // Получаем список непопулярных продуктов.
        String bottomProductsReport = reportGenerator.UnpopularProductsReport(bottomProducts, productSalesCount); 
        System.out.println(bottomProductsReport); 
        reportGenerator.generateReport(UNPOPULAR_PRODUCTS_REPORT, "Отчет о пяти самых непопулярных товарах: \n" + bottomProductsReport); 
    }

    private static void CustomerSpending(Sales analyzer, ReportGenerator reportGenerator, Scanner scanner) {
        System.out.print("Введите сумму для покупателей: ");
        double threshold = scanner.nextDouble(); // Читаем пороговую сумму.
        List<Customer> topCustomers = analyzer.getTopCustomers(threshold); // Список покупателей с суммой больше порога.
        Map<Integer, Long> customerPurchaseCount = analyzer.getCustomerPurchaseCount(); // Количество покупок по клиентам.
        Map<Integer, Double> customerSpending = analyzer.getCustomerSpending(); // Суммы покупок по клиентам.
        String topCustomersReport = reportGenerator.CustomersReport(topCustomers, customerPurchaseCount, customerSpending); 
        System.out.println(topCustomersReport);
        reportGenerator.generateReport(CUSTOMERS_REPORT, "Отчет о покупателях\n" + topCustomersReport); 
    }

    private static void MonthlySalesTrends(Sales analyzer, ReportGenerator reportGenerator) {
        Map<String, Double> monthlyTrends = analyzer.getMonthlySalesTrends(); // Получаем данные о продажах по месяцам.
        StringBuilder trendsReport = new StringBuilder("Тенденции продаж\n");
        monthlyTrends.forEach((month, amount) -> trendsReport.append("Месяц: " + month + "        Сумма продаж: " + amount + "\n")); 
        System.out.println(trendsReport.toString());
        reportGenerator.generateReport(SALES_TRENDS_REPORT, trendsReport.toString()); // Генерируем отчет.
    }

    // Методы для загрузки данных:
    private static List<Sale> loadSales(String filename) throws IOException {
        return loadData(filename, Sale::fromCSV); // Читаем данные о продажах.
    }

    private static List<Product> loadProducts(String filename) throws IOException {
        return loadData(filename, Product::fromCSV); // Читаем данные о продуктах.
    }

    private static List<Customer> loadCustomers(String filename) throws IOException {
        return loadData(filename, Customer::fromCSV); // Читаем данные о клиентах.
    }

    // Универсальный метод для загрузки данных из файла.
    private static <T> List<T> loadData(String filename, CSVParser<T> parser) throws IOException {
        List<T> items = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filename)); // Читаем строки файла.
        for (int i = 1; i < lines.size(); i++) { // Пропускаем заголовок.
            items.add(parser.parse(lines.get(i))); // Парсим каждую строку.
        }
        return items; // Возвращаем список объектов.
    }

    // Интерфейс для парсинга строк CSV.
    interface CSVParser<T> {
        T parse(String line); // Метод для преобразования строки в объект.
    }
}