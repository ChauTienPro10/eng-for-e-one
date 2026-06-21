package englisg.everyone.securityService.controller;

import englisg.everyone.securityService.dtos.RegisterRequest;
import englisg.everyone.securityService.services.keycloak.KeycloakUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keycloak-api")
public class CallKeyCloakController {
    private final KeycloakUserService keycloakUserService;

    public CallKeyCloakController(KeycloakUserService keycloakUserService) {
        this.keycloakUserService = keycloakUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterRequest request) {

        try {
            String status = keycloakUserService.createUserOnKeycloak(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
