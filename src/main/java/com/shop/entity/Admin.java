package com.shop.entity;

import com.shop.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Column(name = "admin_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
