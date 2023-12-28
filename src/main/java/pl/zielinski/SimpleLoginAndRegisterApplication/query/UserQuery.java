package pl.zielinski.SimpleLoginAndRegisterApplication.query;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public class UserQuery {
    public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password, age) VALUES(:firstName, :lastName, :email, :password, :age)";
}
