package br.com.compass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import br.com.compass.model.User;
import br.com.compass.dao.UserDAO;
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
            scanner.nextLine();
            System.out.println("Option selected: " + option); // Log da opção escolhida

            switch (option) {
                case 1:
                    bankMenu(scanner);
                    return;
                case 2:
                    System.out.println("Account Opening.");
                    openAccount(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
                    break;
            }
        }
    }

    public static void openAccount(Scanner scanner) {
        try {
            System.out.println("=== Account Opening ===");

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Name entered: " + name); // Log do nome

            System.out.print("Enter your date of birth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine();
            System.out.println("Date of birth entered: " + dobInput); // Log da data de nascimento
            Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dobInput);

            System.out.print("Enter your CPF: ");
            String cpf = scanner.nextLine();
            System.out.println("CPF entered: " + cpf); // Log do CPF

            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            System.out.println("Phone entered: " + phone); // Log do telefone

            System.out.print("Enter your account type (Savings or Current): ");
            String acountType = scanner.nextLine();
            System.out.println("Account type selected: " + acountType); // Log do tipo de conta

            User user = new User();
            user.setName(name);
            user.setDate(dateOfBirth);
            user.setCpf(cpf);
            user.setPhone(phone);
            user.setAcountType(acountType);

            // Log de criação do objeto User
            System.out.println("User object created: " + user);

            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.saveUser(user);
            System.out.println("User save operation result: " + success); // Log do resultado da operação de salvar
                                                                          // usuário

            if (success) {
                System.out.println("Account created successfully!");
            } else {
                System.out.println("Error creating account. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while opening the account. Please try again.");
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
            System.out.println("Option selected in bank menu: " + option); // Log da opção escolhida no menu do banco

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
                    break;
            }
        }
    }
}
