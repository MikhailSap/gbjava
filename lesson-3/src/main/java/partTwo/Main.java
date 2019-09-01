package partTwo;

public class Main {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Lomachenko", 1234567);
        phoneBook.add("Yarskaya", 3333333);
        phoneBook.add("Yarskaya", 8888888);

        System.out.println(phoneBook.get("Lomachenko"));
        System.out.println(phoneBook.get("Yarskaya"));
    }
}
