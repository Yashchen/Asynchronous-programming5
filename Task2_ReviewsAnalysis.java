import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Task2_ReviewsAnalysis {
    public static void main(String[] args) {
        System.out.println("--- Початок Завдання 2 ---");

        CompletableFuture<String> serverUS = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return "US-Server: ID-12345";
        });

        CompletableFuture<String> serverEU = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return "EU-Server: ID-12345";
        });

        CompletableFuture<Object> fastIdFuture = CompletableFuture.anyOf(serverUS, serverEU);

        CompletableFuture<String> finalReport = fastIdFuture.thenCompose(resultObj -> {
            String productId = (String) resultObj;
            System.out.println("Знайдено продукт через: " + productId);

            return collectAndAnalyzeReviews(productId);
        });

        System.out.println("Підсумок: " + finalReport.join());
    }

    private static CompletableFuture<String> collectAndAnalyzeReviews(String productId) {
        CompletableFuture<String> platform1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Завантаження з Google Reviews...");
            sleep(1);
            return "Google: 4.5/5 (Чудово)";
        });

        CompletableFuture<String> platform2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Завантаження з Rozetka...");
            sleep(1);
            return "Rozetka: 4.0/5 (Добре)";
        });

        CompletableFuture<String> platform3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Завантаження з Amazon...");
            sleep(2);
            return "Amazon: 4.2/5 (Нормально)";
        });

        return platform1
                .thenCombine(platform2, (review1, review2) -> {
                    return "1. " + review1 + "\n" +
                            "2. " + review2 + "\n";
                })
                .thenCombine(platform3, (reviews1and2, review3) -> {
                    return "\nАналіз для " + productId + ":\n" +
                            reviews1and2 +
                            "3. " + review3 + "\n" +
                            "Висновок: Продукт має позитивний рейтинг на 3-х платформах.";
                });
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}