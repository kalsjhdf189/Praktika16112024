import java.io.IOException; // Импорт исключения для обработки ошибок ввода/вывода.
import java.nio.file.*; // Импорт классов для работы с файлами и путями.
import java.util.List; // Импорт интерфейса List для работы со списками.
import java.util.Map; // Импорт интерфейса Map для работы с картами (ключ-значение).

public class ReportGenerator { // Определение публичного класса для генерации отчетов.

    public void generateReport(String filename, String content) { 
        // Метод для записи содержимого отчета в файл.
        try {
            Files.write(Paths.get(filename), content.getBytes()); 
            // Запись данных в файл. Если файл не существует, он будет создан.
        } catch (IOException e) { 
            // Обработка исключений ввода/вывода.
            e.printStackTrace(); 
            // Вывод стека вызовов для отладки.
        }
    }

    public String PopularProductsReport(List<Product> products, Map<Integer, Long> productSalesCount) {
        // Метод для создания отчета о самых популярных продуктах.
        StringBuilder report = new StringBuilder("Пять самых популярных товаров:\n"); 
        // Создание строки-отчета с заголовком.
        for (Product product : products) { 
            // Проход по списку популярных продуктов.
            long salesCount = productSalesCount.getOrDefault(product.getProductId(), 0L); 
            // Получение количества продаж для каждого продукта. Если данных нет, используется 0.
            report.append("ID: ").append(product.getProductId()) 
                  // Добавление ID продукта.
                  .append(", Название: ").append(product.getProductName()) 
                  // Добавление названия продукта.
                  .append(", Цена: ").append(product.getPrice()) 
                  // Добавление цены продукта.
                  .append(", Количество продаж: ").append(salesCount).append("\n"); 
                  // Добавление количества продаж.
        }
        return report.toString(); 
        // Возврат отчета в виде строки.
    }

    public String UnpopularProductsReport(List<Product> products, Map<Integer, Long> productSalesCount) {
        // Метод для создания отчета о наименее популярных продуктах.
        StringBuilder report = new StringBuilder("Пять самых непопулярных товаров:\n");
        for (Product product : products) {
            long salesCount = productSalesCount.getOrDefault(product.getProductId(), 0L);
            // Аналогично методу PopularProductsReport.
            report.append("ID: ").append(product.getProductId())
                  .append(", Название: ").append(product.getProductName())
                  .append(", Цена: ").append(product.getPrice())
                  .append(", Количество продаж: ").append(salesCount).append("\n");
        }
        return report.toString();
    }

    public String CustomersReport(List<Customer> topCustomers, Map<Integer, Long> customerPurchaseCount, Map<Integer, Double> customerSpending) {
        // Метод для создания отчета о покупателях.
        StringBuilder report = new StringBuilder("Отчет о покупателях:\n");
        
        for (Customer customer : topCustomers) { 
            // Проход по списку лучших клиентов.
            long purchaseCount = customerPurchaseCount.getOrDefault(customer.getCustomerId(), 0L); 
            // Получение количества покупок для клиента.
            double totalSpent = customerSpending.getOrDefault(customer.getCustomerId(), 0.0); 
            // Получение общей суммы затрат клиента.
            report.append("Покупатель: ").append(customer.getCustomerName()) 
                  // Добавление имени покупателя.
                  .append(", Количество покупок: ").append(purchaseCount) 
                  // Добавление количества покупок.
                  .append(", Общая сумма потраченных средств: ").append(totalSpent) 
                  // Добавление общей суммы затрат.
                  .append(" руб.\n"); 
                  // Добавление валюты и новой строки.
        }

        return report.toString(); 
        // Возврат отчета в виде строки.
    }

    public String TotalSalesReport(double totalAmount) {
        // Метод для создания отчета о суммарных продажах.
        return "Общая сумма продаж: " + totalAmount; 
        // Возврат строки с информацией о сумме продаж.
    }
}
