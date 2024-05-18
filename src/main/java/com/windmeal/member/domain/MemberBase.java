package com.windmeal.member.domain;

import com.windmeal.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;
    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    public MemberBase(String email, Authority authority) {
        this.email = email;
        this.authority = authority;
    }

    public MemberBase(Long id, String email, Authority authority) {
        this.id = id;
        this.email = email;
        this.authority = authority;
    }

    public MemberBase(Long id) {
        this.id = id;
    }

    public void deleteAccount() {
        isDeleted = true;
    }

    public void restoreAccount() {
        isDeleted = false;
    }
}
