import java.util.*; // Импорт всех классов из пакета java.util, таких как List, Map, HashMap, и других коллекций.
import java.util.logging.Logger; // Импорт класса Logger для логирования.
import java.util.stream.Collectors; // Импорт утилит для работы с потоками (stream API) и преобразования данных.
import java.time.format.TextStyle; // Импорт TextStyle для форматирования дат.

public class Sales { // Определение публичного класса Sales.
    private static final Logger logger = Logger.getLogger(Sales.class.getName()); 
    // Статический логгер для логирования сообщений, таких как отладка или информация о ходе программы.

    private List<Sale> sales; // Список продаж.
    private List<Product> products; // Список продуктов.
    private List<Customer> customers; // Список клиентов.

    public Sales(List<Sale> sales, List<Product> products, List<Customer> customers) {
        this.sales = sales; // Инициализация списка продаж.
        this.products = products; // Инициализация списка продуктов.
        this.customers = customers; // Инициализация списка клиентов.
    }

    public double getTotalSalesAmount() {
        logger.info("Calculating the total amount of sales..."); 
        // Логирование начала выполнения метода.
        return sales.stream() // Преобразование списка продаж в поток данных.
                .mapToDouble(sale -> getProductById(sale.getProductId()).getPrice()) 
                // Для каждой продажи получение цены продукта и преобразование в поток double.
                .sum(); // Суммирование всех цен продуктов.
    }

    public List<Product> getTopProducts() {
        Map<Integer, Long> productSalesCount = sales.stream() 
            // Создание потока из списка продаж.
            .collect(Collectors.groupingBy(Sale::getProductId, Collectors.counting())); 
            // Группировка продаж по ID продуктов и подсчет их количества.

        return productSalesCount.entrySet().stream() 
            // Создание потока из пар ключ-значение (ID продукта и количество продаж).
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) 
            // Сортировка продуктов по количеству продаж в убывающем порядке.
            .limit(5) 
            // Ограничение результатов до 5 самых продаваемых продуктов.
            .map(entry -> getProductById(entry.getKey())) 
            // Получение объекта продукта по его ID.
            .collect(Collectors.toList()); 
            // Преобразование результатов в список.
    }

    public List<Product> getUnpopularProducts() {
        Map<Integer, Long> productSalesCount = sales.stream()
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.counting()));
        // Аналогично getTopProducts, подсчитывается количество продаж продуктов.

        return productSalesCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) 
                // Сортировка продуктов по количеству продаж в возрастающем порядке.
                .limit(5) 
                // Ограничение до 5 наименее продаваемых продуктов.
                .map(entry -> getProductById(entry.getKey())) 
                // Преобразование ID продукта в объект продукта.
                .collect(Collectors.toList()); 
                // Преобразование в список.
    }

    public Map<Integer, Double> getCustomerSpending() {
        Map<Integer, Double> customerSpending = new HashMap<>(); 
        // Создание карты для хранения суммы затрат каждого клиента.
        for (Sale sale : sales) { 
            // Проход по всем продажам.
            double productPrice = products.stream() 
                    .filter(p -> p.getProductId() == sale.getProductId()) 
                    // Поиск продукта по его ID.
                    .map(Product::getPrice) 
                    // Получение цены продукта.
                    .findFirst()
                    .orElse(0.0); 
                    // Если продукт не найден, используется цена 0.0.
            customerSpending.merge(sale.getCustomerId(), productPrice, Double::sum); 
            // Обновление карты: добавление цены продукта к затратам клиента.
        }
        return customerSpending; 
        // Возврат карты с данными о затратах клиентов.
    }

    public List<Customer> getTopCustomers(double threshold) {
        Map<Integer, Double> customerSpending = getCustomerSpending(); 
        // Получение затрат клиентов.

        return customerSpending.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold) 
                // Фильтрация клиентов, чьи затраты превышают или равны заданному порогу.
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue())) 
                // Сортировка клиентов по убыванию их затрат.
                .map(entry -> customers.stream()
                        .filter(c -> c.getCustomerId() == entry.getKey()) 
                        // Поиск клиента по его ID.
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull) 
                // Исключение null-значений (если клиент не найден).
                .collect(Collectors.toList()); 
                // Преобразование в список.
    }

    public Map<String, Double> getMonthlySalesTrends() {
        Map<String, Double> monthlyTrends = new HashMap<>(); 
        // Создание карты для хранения месячных трендов продаж.

        for (Sale sale : sales) { 
            // Проход по всем продажам.
            String month = sale.getSaleDateTime().getMonth().getDisplayName(TextStyle.SHORT, new Locale("ru", "RU")); 
            // Получение названия месяца продажи на русском языке.
            Product product = getProductById(sale.getProductId()); 
            // Получение объекта продукта по его ID.
            monthlyTrends.merge(month, product.getPrice(), Double::sum); 
            // Обновление карты: добавление цены продукта к продажам месяца.
        }

        return monthlyTrends; 
        // Возврат карты с трендами продаж по месяцам.
    }

    public Map<Integer, Long> getProductSalesCount() {
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.counting())); 
                // Подсчет количества продаж для каждого продукта.
    }

    public Map<Integer, Long> getCustomerPurchaseCount() {
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getCustomerId, Collectors.counting())); 
                // Подсчет количества покупок для каждого клиента.
    }

    private Product getProductById(int productId) {
        return products.stream()
                .filter(p -> p.getProductId() == productId) 
                // Поиск продукта по его ID.
                .findFirst()
                .orElse(null); 
                // Если продукт не найден, возвращается null.
    }
}
