package com.process.clash.adapter.persistence.auth;

import com.process.clash.domain.user.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserDto implements Serializable {
    private String username;
    private String email;
    private String name;
    private String encodedPassword;

    public static PendingUserDto from(User user) {
        return new PendingUserDto(
                user.username(),
                user.email(),
                user.name(),
                user.password() // 암호화된 비밀번호
        );
    }

    public User toUser() {
        return User.createDefault(username, email, name, encodedPassword);
    }
}