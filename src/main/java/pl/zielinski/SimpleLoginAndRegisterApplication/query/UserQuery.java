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
    public static final String UPDATE_USER_DATA_QUERY = "UPDATE Users SET first_name = :firstName, last_name = :lastName, email = :email, age = :age WHERE id = :id";
    public static final String SELECT_USERS_QUERY = "SELECT * FROM Users";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)";
    public static final String SELECT_USER_BY_ACCOUNT_URL_QUERY = "SELECT * FROM Users WHERE id = (SELECT user_id FROM AccountVerifications WHERE url = :url)";
    public static final String DELETE_USER_IN_ACCOUNT_VERIFICATIONS_BY_KEY_QUERY = "DELETE FROM AccountVerifications WHERE url = :key";
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID = "DELETE FROM TwoFactorVerifications WHERE user_id = :id";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO TwoFactorVerifications(user_id, code, expire_date) VALUES(:id, :url, :expiration)";

}
