package com.shop.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.constant.Role;
import com.shop.dto.MemberASDto;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;


    private String address;
    private String provider;
    private String tel;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Reservation> reservationList = new ArrayList<>();
    public Member() {
    }

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
        this.role = Role.USER;
    }

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setTel(memberFormDto.getTel());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(memberFormDto.getRole());
        return member;
    }
    public Member update(String name, Role role) {
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
        return this;
    }
    public Member MemberAS(MemberASDto memberASDto) {
        this.address = memberASDto.getAddress();
        this.tel = memberASDto.getTel();
        return this;
    }

}
