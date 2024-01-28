import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseProgram {
    private static final String FILE_PATH = "database.txt";
    private static final Scanner scanner = new Scanner(System.in);

    static class Person {
        int id;
        String name;
        int age;

        public Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return id + "," + name + "," + age;
        }

        static Person fromString(String line) {
            String[] parts = line.split(",");
            return new Person(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]));
        }
    }

    private static List<Person> readDatabase() throws IOException {
        List<Person> people = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                people.add(Person.fromString(line));
            }
        }
        return people;
    }

    private static void writeDatabase(List<Person> people) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Person person : people) {
                writer.write(person.toString());
                writer.newLine();
            }
        }
    }

    private static void addPerson() throws IOException {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        List<Person> people = readDatabase();
        people.add(new Person(id, name, age));
        writeDatabase(people);
        System.out.println("Person added successfully!");
    }

    private static void viewPeople() throws IOException {
        List<Person> people = readDatabase();
        if (people.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Person person : people) {
            System.out.println(person);
        }
    }

    private static void searchPeople() throws IOException {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase();

        List<Person> people = readDatabase();
        List<Person> filteredPeople = people.stream()
            .filter(p -> p.name.toLowerCase().contains(query) || String.valueOf(p.id).equals(query))
            .collect(Collectors.toList());

        if (filteredPeople.isEmpty()) {
            System.out.println("No matching records found.");
        } else {
            filteredPeople.forEach(System.out::println);
        }
    }

    private static void updatePerson() throws IOException {
        System.out.print("Enter ID of person to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        List<Person> people = readDatabase();
        for (Person person : people) {
            if (person.id == id) {
                System.out.print("Enter new Name: ");
                person.name = scanner.nextLine();
                System.out.print("Enter new Age: ");
                person.age = Integer.parseInt(scanner.nextLine());
                writeDatabase(people);
                System.out.println("Person updated successfully!");
                return;
            }
        }
        System.out.println("Person not found.");
    }

    private static void deletePerson() throws IOException {
        System.out.print("Enter ID of person to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        List<Person> people = readDatabase();
        if (people.removeIf(p -> p.id == id)) {
            writeDatabase(people);
            System.out.println("Person deleted successfully!");
        } else {
            System.out.println("Person not found.");
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("\nOptions: 1 - Add, 2 - View, 3 - Search, 4 - Update, 5 - Delete, 0 - Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addPerson();
                        break;
                    case 2:
                        viewPeople();
                        break;
                    case 3:
                        searchPeople();
                        break;
                    case 4:
                        updatePerson();
                        break;
                    case 5:
                        deletePerson();
                        break;
                    case 0:
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
