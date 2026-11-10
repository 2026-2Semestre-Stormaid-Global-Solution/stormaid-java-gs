package br.com.stormaid.entity;

import br.com.stormaid.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_STORMAID_ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME", length = 30, nullable = false, unique = true)
    private UserRole name;
}