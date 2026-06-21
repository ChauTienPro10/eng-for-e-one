package englisg.everyone.userManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@Builder
public class User {
    @Id
    @Column(name = "id", length = 36)
    private String id;
    private String username;
    private Long createdAt;
    private Long updatedAt;
    private String profileId;
}
