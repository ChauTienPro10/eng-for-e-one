package englisg.everyone.securityService.services.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;

    // Đọc tên Realm đích (app-prod) từ file properties để biết sẽ tạo user vào đâu
    @Value("${keycloak.target.realm}")
    private String targetRealm;

    // Tiêm (Inject) Bean Keycloak đã cấu hình ở bước trước vào đây
    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public String createUserOnKeycloak(String username, String email, String password) {
        // 1. Định nghĩa thông tin tài khoản người dùng mới
        UserRepresentation user = getUserRepresentation(username, email, password);

        // 3. Gọi tới phân vùng quản lý User của Realm đích và thực thi lệnh tạo
        UsersResource usersResource = keycloak.realm(targetRealm).users();
        Response response = usersResource.create(user);

        // 4. Kiểm tra mã phản hồi (HTTP Status) trả về từ Keycloak Server
        if (response.getStatus() == 201) {
            return "Tạo thành công user: " + username;
        } else if (response.getStatus() == 409) {
            throw new RuntimeException("Lỗi: Tên đăng nhập (Username) hoặc Email đã tồn tại trên Keycloak!");
        } else {
            throw new RuntimeException("Thất bại! Mã lỗi hệ thống Keycloak: " + response.getStatus());
        }
    }

    private static UserRepresentation getUserRepresentation(String username, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setEmailVerified(true); // Tự động kích hoạt trạng thái xác thực Email

        // 2. Định nghĩa mật khẩu cho tài khoản này
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false); // false = người dùng không cần đổi mật khẩu ở lần đăng nhập đầu tiên
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }
}