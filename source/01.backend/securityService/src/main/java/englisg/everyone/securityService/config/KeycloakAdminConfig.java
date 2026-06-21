package englisg.everyone.securityService.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    // Ánh xạ các giá trị từ file application.properties vào biến Java
    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    /**
     * Khởi tạo Bean Keycloak Admin Client để Spring Boot sử dụng ở các Service khác.
     * Bean này đóng vai trò như một "kết nối quản trị" giúp bạn thực hiện các thao tác
     * như tạo user, xóa user, gán role trên Keycloak Server.
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm) // Thường là 'master' để có toàn quyền quản trị
                .username(username) // Tài khoản admin của Keycloak
                .password(password) // Mật khẩu của tài khoản admin
                .clientId(clientId) // Mặc định dùng 'admin-cli' để gọi API bằng code
                .build();
    }
}