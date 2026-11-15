package org.csu.mypetstore.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.entity.User;
import org.csu.mypetstore.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<UserReadResult> findByUsername(String username) {
        var query = Wrappers.lambdaQuery(User.class)
                .select(User::getId, User::getUsername, User::getDisplayName, User::getRole, User::getStatus)
                .eq(User::getUsername, username);

        return Optional.ofNullable(userMapper.selectOne(query))
                .map(user -> new UserReadResult(
                        user.getId(), user.getUsername(), user.getDisplayName(), user.getRole(), user.getStatus()
                ));
    }
}
