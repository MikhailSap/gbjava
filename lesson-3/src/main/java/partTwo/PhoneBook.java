package partTwo;

import java.util.*;

public class PhoneBook {
    private Map<String, Set<Integer>> phoneBook = new HashMap<>();

    public void add(String surname, int phoneNumber) {
        if (!phoneBook.containsKey(surname)) {
            Set<Integer> phoneNumbers = new HashSet<>();
            phoneNumbers.add(phoneNumber);
            phoneBook.put(surname, phoneNumbers);
        } else {
            phoneBook.get(surname).add(phoneNumber);
        }
    }

    public Set<Integer> get(String surname) {
        return phoneBook.get(surname);
    }
}
