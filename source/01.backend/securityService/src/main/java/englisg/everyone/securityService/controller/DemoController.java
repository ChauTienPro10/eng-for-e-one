package englisg.everyone.securityService.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    // Ai cũng truy cập được
    @GetMapping("/public/hello")
    public String publicEndpoint() {
        return "Đây là API công khai, không cần Token!";
    }

    // Phải đăng nhập thành công mới vào được
    @GetMapping("/secured")
    public String securedEndpoint() {
        return "Bạn đã đăng nhập thành công vào hệ thống!";
    }

    // Phải đăng nhập và có Role 'ADMIN' trên Keycloak mới vào được
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Chào Admin! Đây là dữ liệu tối mật.";
    }
}