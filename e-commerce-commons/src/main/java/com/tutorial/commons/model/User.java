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
     * User id
     */
    private int id;

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
     * Parameterized constructor that maps that data from a @{@link ResultSet}
     * to a @{@link User} object.
     *
     * @param rs The resultSet obtained from the database.
     * @throws SQLException
     */
    public User(ResultSet rs) throws SQLException {
        Assert.notNull(rs, "Result Set should not be null");
        this.id = rs.getInt("id");
        this.name = rs.getString("user_name");
        this.email = rs.getString("email");
        this.phone = rs.getLong("phone");
        this.userType = rs.getString("user_type").equals("C") ? UserType.CUSTOMER : UserType.VENDOR;
    }

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
