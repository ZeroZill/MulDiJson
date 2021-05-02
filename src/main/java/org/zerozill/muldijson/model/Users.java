package org.zerozill.muldijson.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// reference:
// https://github.com/fabienrenaud/java-json-benchmark/blob/master/src/main/java/com/github/fabienrenaud/jjb/model/Users.java

@JsonObject
public class Users {

    @JsonField
    public List<User> users;

    public Users() {

    }

    public Users(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;

        Users that = (Users) o;

        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return users != null ? users.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Users{" + "users=" + users + '}';
    }

    @JsonObject
    public static final class User {

        @JsonField
        public String _id;
        @JsonField
        public int index;
        @JsonField
        public String guid;
        @JsonField
        public long lastOnline;
        @JsonField
        public boolean isActive;
        @JsonField
        public String balance;
        @JsonField
        public String picture;
        @JsonField
        public int age;
        @JsonField
        public String eyeColor;
        @JsonField
        public String name;
        @JsonField
        public String gender;
        @JsonField
        public String company;
        @JsonField
        public String email;
        @JsonField
        public String phone;
        @JsonField
        public String address;
        @JsonField
        public String about;
        @JsonField
        public String registered;
        @JsonField
        public double latitude;
        @JsonField
        public double longitude;
        @JsonField
        public List<String> tags;
        @JsonField
        public List<Friend> friends;

        public User() {

        }

        public User(String _id, int index, String guid, long lastOnline, boolean isActive, String balance, String picture, int age, String eyeColor, String name, String gender, String company, String email, String phone, String address, String about, String registered, double latitude, double longitude, List<String> tags, List<Friend> friends) {
            this._id = _id;
            this.index = index;
            this.guid = guid;
            this.lastOnline = lastOnline;
            this.isActive = isActive;
            this.balance = balance;
            this.picture = picture;
            this.age = age;
            this.eyeColor = eyeColor;
            this.name = name;
            this.gender = gender;
            this.company = company;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.about = about;
            this.registered = registered;
            this.latitude = latitude;
            this.longitude = longitude;
            this.tags = tags;
            this.friends = friends;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof User)) {
                return false;
            }
            User user = (User) o;
            return index == user.index &&
                    isActive == user.isActive &&
                    age == user.age &&
                    Math.abs(Double.doubleToLongBits(user.latitude) - Double.doubleToLongBits(latitude)) < 3 &&
                    Math.abs(Double.doubleToLongBits(user.longitude) - Double.doubleToLongBits(longitude)) < 3 &&
                    Objects.equals(_id, user._id) &&
                    Objects.equals(guid, user.guid) &&
                    Objects.equals(lastOnline, user.lastOnline) &&
                    Objects.equals(balance, user.balance) &&
                    Objects.equals(picture, user.picture) &&
                    Objects.equals(eyeColor, user.eyeColor) &&
                    Objects.equals(name, user.name) &&
                    Objects.equals(gender, user.gender) &&
                    Objects.equals(company, user.company) &&
                    Objects.equals(email, user.email) &&
                    Objects.equals(phone, user.phone) &&
                    Objects.equals(address, user.address) &&
                    Objects.equals(about, user.about) &&
                    Objects.equals(registered, user.registered) &&
                    Objects.equals(tags, user.tags) &&
                    Objects.equals(friends, user.friends);
        }

        @Override
        public int hashCode() {
            return Objects.hash(_id, index, guid, lastOnline, isActive, balance, picture, age, eyeColor, name, gender, company, email, phone, address, about, registered, tags, friends);
        }

        @Override
        public String toString() {
            return "JsonDataObj{" + "_id=" + _id + ", index=" + index + ", guid=" + guid + ", lastOnline=" + lastOnline + ", isActive=" + isActive + ", balance=" + balance + ", picture=" + picture + ", age=" + age + ", eyeColor=" + eyeColor + ", name=" + name + ", gender=" + gender + ", company=" + company + ", email=" + email + ", phone=" + phone + ", address=" + address + ", about=" + about + ", registered=" + registered + ", latitude=" + latitude + ", longitude=" + longitude + ", tags=" + tags + ", friends=" + friends + ", greeting=" + '}';
        }
    }

    @JsonObject
    public static final class Friend {

        @JsonField
        public String id;
        @JsonField
        public String name;
        @JsonField
        public long lastOnline;

        public Friend() {
        }

        public static Friend create(String id, String name, long lastOnline) {
            Friend friend = new Friend();
            friend.id = id;
            friend.name = name;
            friend.lastOnline = lastOnline;
            return friend;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Friend)) return false;

            Friend friend = (Friend) o;

            if (!Objects.equals(id, friend.id)) return false;
            return Objects.equals(name, friend.name) && Objects.equals(lastOnline, friend.lastOnline);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = (int) (31 * result + lastOnline >> 32);
            result = (int) (31 * result + lastOnline - lastOnline >> 32 << 32);
            return result;
        }

        @Override
        public String toString() {
            return "Friend{" + "id=" + id + ", name=" + name + '}';
        }

    }

}
