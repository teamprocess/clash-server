package com.process.clash.adapter.persistence.user;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Major major;

    // helper to allow creating a lightweight reference by id
    public static UserJpaEntity ofId(Long id) {
        UserJpaEntity e = new UserJpaEntity();
        try {
            java.lang.reflect.Field f = UserJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {}
        return e;
    }
}