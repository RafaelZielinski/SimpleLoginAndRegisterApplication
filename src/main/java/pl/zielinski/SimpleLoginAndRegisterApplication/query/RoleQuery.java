package pl.zielinski.SimpleLoginAndRegisterApplication.query;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 25/12/2023
 */
public class RoleQuery {
    public static final String SELECT_ROLES_QUERY = "SELECT * FROM Roles;";
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName;";
    public static final String SELECT_ROLE_BY_USER_ID = "SELECT * FROM Roles r JOIN UserRoles ur ON ur.role_id = r.id JOIN Users u ON u.id = ur.user_id WHERE user_id = :id;";
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles(user_id, role_id) VALUES(:userId, :roleId);";
    public static final String SELECT_ROLE_BY_ID = "SELECT * FROM Roles WHERE id = :id;";
    public static final String UPDATE_USER_ENABLED_QUERY = "UPDATE Users SET enabled = :enabled WHERE id = :id";

}
