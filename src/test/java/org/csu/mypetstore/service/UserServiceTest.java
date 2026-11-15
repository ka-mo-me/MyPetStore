package org.csu.mypetstore.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.entity.User;
import org.csu.mypetstore.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockMakers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(mockMaker = MockMakers.PROXY)
    private UserMapper userMapper;

    private UserService userService;

    @BeforeAll
    static void initializeUserMetadata() {
        if (TableInfoHelper.getTableInfo(User.class) == null) {
            var assistant = new MapperBuilderAssistant(new MybatisConfiguration(), UserMapper.class.getName());
            assistant.setCurrentNamespace(UserMapper.class.getName());
            TableInfoHelper.initTableInfo(assistant, User.class);
        }
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(userMapper);
    }

    @Test
    void findByUsernameMapsUserToReadResult() {
        var user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setDisplayName("Alice");
        user.setRole("CUSTOMER");
        user.setStatus("ENABLED");
        when(userMapper.selectOne(any())).thenReturn(user);

        var result = userService.findByUsername("alice");

        assertEquals(Optional.of(new UserReadResult(1L, "alice", "Alice", "CUSTOMER", "ENABLED")), result);
    }

    @Test
    void findByUsernameReturnsEmptyWhenUserIsMissing() {
        when(userMapper.selectOne(any())).thenReturn(null);

        assertTrue(userService.findByUsername("not-exists").isEmpty());
    }

    @Test
    void findByUsernamePropagatesSameDataAccessException() {
        var failure = new DataAccessResourceFailureException("Simulated mapper failure");
        when(userMapper.selectOne(any())).thenThrow(failure);

        var thrown = assertThrows(DataAccessResourceFailureException.class,
                () -> userService.findByUsername("alice"));

        assertSame(failure, thrown);
    }
}
