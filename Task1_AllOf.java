import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Task1_AllOf {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("--- Початок Завдання 1 ---");

        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return "Дані з Сервера А";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return "Дані з Сервера Б";
        });

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return "Дані з Сервера В";
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

        allTasks.thenRun(() -> {
            try {
                String result1 = task1.get();
                String result2 = task2.get();
                String result3 = task3.get();

                System.out.println("Всі завдання виконано:");
                System.out.println("- " + result1);
                System.out.println("- " + result2);
                System.out.println("- " + result3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).join();
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}