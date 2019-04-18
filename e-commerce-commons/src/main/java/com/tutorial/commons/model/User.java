package com.tutorial.commons.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Simple POJO to hold user data.
 */
@Getter
@Setter
@NoArgsConstructor
public class User {

    /**
     * Static util method to create a dummy user. Useful for mocking during development.
     *
     * @return Dummy user
     */
    public static User createDummyUser() {
        User user = new User();
        user.id = -1;
        user.name = "John Doe";
        user.email = "johnDoe@test.com";
        user.phone = 9999999999l;
        user.userType = UserType.CUSTOMER;
        return user;
    }

    /**
     * Parameterized constructor that maps the data from a @{@link ResultSet}
     * to a @{@link User} object.
     *
     * @param rs The resultSet obtained from the database.
     * @throws SQLException
     */
    public User(ResultSet rs) throws SQLException {
        Assert.notNull(rs, "Result Set should not be null");
        this.id = rs.getInt("user_id");
        this.name = rs.getString("user_name");
        this.email = rs.getString("email");
        this.phone = rs.getLong("phone_num");
        this.userType = rs.getString("user_type").equals("C") ? UserType.CUSTOMER : UserType.VENDOR;
    }

    /**
     * User id
     */
    private long id;

    /**
     * User name
     */
    private String name;

    /**
     * User email
     */
    private String email;

    /**
     * User phone number
     */
    private long phone;

    /**
     * Field denoting the type of user i.e. vendor OR customer
     */
    private UserType userType;

    /**
     * Placeholder to hold the user type.
     */
    public enum UserType {

        CUSTOMER("C"), VENDOR("V");

        private String value;

        UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
