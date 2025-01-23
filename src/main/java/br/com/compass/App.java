package br.com.compass;

import java.util.Scanner;
import br.com.compass.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {

    public static void main(String[] args) {
        // Configurar e inicializar o Hibernate
        initializeDatabase();

        // Scanner para entrada do usuário
        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner);

        scanner.close();
        System.out.println("Application closed");
    }

    public static void initializeDatabase() {
        // Configuração do Hibernate
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            System.out.println("Conexão com o banco de dados estabelecida!");
            System.out.println("Tabela 'users' criada/atualizada no banco de dados.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao inicializar o banco de dados.");
        }
    }

    public static void mainMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    bankMenu(scanner);
                    return;
                case 2:
                    System.out.println("Account Opening.");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void bankMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Deposit.");
                    break;
                case 2:
                    System.out.println("Withdraw.");
                    break;
                case 3:
                    System.out.println("Check Balance.");
                    break;
                case 4:
                    System.out.println("Transfer.");
                    break;
                case 5:
                    System.out.println("Bank Statement.");
                    break;
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
