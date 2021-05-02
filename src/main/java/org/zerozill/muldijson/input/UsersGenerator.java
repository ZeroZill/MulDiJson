package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Carriage;
import org.zerozill.muldijson.model.Users;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class UsersGenerator extends InputGenerator {
    private static final String[] firstName = new String[]{"Albert", "Bob", "Cart", "Deb", "Funky", "Elvin", "Gean", "Howl", "Illis", "Jack", "Kate", "Lumine", "Mike", "Nickle", "Orb", "Peter", "Richeal", "Santo", "Tina", "XenoBlade"};
    private static final String[] lastName = new String[]{"Jacobb", "Atlas", "Daul", "Tediore", "Togue", "Vladof", "Maliwen"};

    private static final String[] tagsArr = new String[]{"Adamant", "Bashful", "Bold", "Brave", "Calm", "Careful", "Docile", "Gentle", "Hardly", "Hasty", "Jolly", "Modest", "Rash", "Timid"};

    private final Users users;
    private final Random random = new Random();

    public UsersGenerator() {
        List<Users.User> usersList = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            usersList.add(genUser(i));
        }
        users = new Users(usersList);
    }

    private Users.User genUser(int i) {
        long registered = random.nextLong();
        long lastOnline = registered + random.nextInt(500000000);
        return new Users.User(
                String.format("100000%04d", i),
                100000 + random.nextInt(1000),
                String.format("User#%04d", i),
                lastOnline,
                random.nextBoolean(),
                Long.toUnsignedString(random.nextLong()),
                String.format("Pic_%s", Long.toUnsignedString(random.nextLong())),
                random.nextInt(50),
                genColor(),
                genName(),
                genGender(),
                String.format("Company-%s", random.nextInt(5)),
                String.format("%d@gmail.com", random.nextLong()),
                String.format("+86-%03d%04d%04d", random.nextInt(1000), random.nextInt(10000), random.nextInt(10000)),
                String.format("%s, Province %s, County %s, %s Street, %d", 65 + random.nextInt(26), 65 + random.nextInt(26), 65 + random.nextInt(26), 65 + random.nextInt(26), random.nextInt(1000)),
                "NaN",
                Long.toString(registered),
                (random.nextDouble() - 0.5) * 90,
                random.nextDouble() * 360,
                genTags(),
                genFriends()
        );
    }

    private String genColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return String.format("#%x%x%x", r, g, b).toUpperCase();
    }

    private String genName() {
        int firstIdx = random.nextInt(firstName.length);
        int lastIdx = random.nextInt(lastName.length);
        return firstName[firstIdx] + " " + lastName[lastIdx];
    }

    private String genGender() {
        return random.nextBoolean() ? "Male" : "Female";
    }

    private List<String> genTags() {
        List<String> tags = new LinkedList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            tags.add(tagsArr[random.nextInt(tagsArr.length)]);
        }
        return tags;
    }

    private List<Users.Friend> genFriends() {
        List<Users.Friend> friends = new LinkedList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            long lastOnline = random.nextLong();
            Users.Friend friend = Users.Friend.create(String.format("100000%04d", i), genName(), lastOnline);
            friends.add(friend);
        }
        return friends;
    }

    @Override
    public String generateJson() {
        return new Gson().toJson(users);
    }

    @Override
    public Object generateBean() {
        return users;
    }

    @Override
    public String toString() {
        return "Users";
    }
}

