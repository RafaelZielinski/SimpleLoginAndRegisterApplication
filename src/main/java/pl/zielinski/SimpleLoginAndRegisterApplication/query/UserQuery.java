package pl.zielinski.SimpleLoginAndRegisterApplication.query;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public class UserQuery {
    public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password, age) VALUES(:firstName, :lastName, :email, :password, :age)";
    public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE id = :id";
    public static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
    public static final String UPDATE_USER_DATA_QUERY = "UPDATE Users SET first_name = :firstName, last_name = :lastName, email = :email, age = :age, password = :password WHERE id = :id";
    public static final String SELECT_USERS_QUERY = "SELECT * FROM Users";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
}
