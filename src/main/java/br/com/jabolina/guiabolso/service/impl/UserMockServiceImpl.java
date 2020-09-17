package br.com.jabolina.guiabolso.service.impl;

import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.mock.MockDataProperties;
import br.com.jabolina.guiabolso.mock.UserProperties;
import br.com.jabolina.guiabolso.service.MockService;
import br.com.jabolina.guiabolso.util.helpers.AssertHelper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

@Service
public class UserMockServiceImpl implements MockService<User> {
    @Override
    public User mock(MockDataProperties mockDataProperties) {
        UserProperties properties = AssertHelper.isInstance(UserProperties.class,
                mockDataProperties, "Is not user properties!");
        User user = new User();
        user.setId(properties.getId());
        user.setTransactions(Lists.newArrayList());
        return user;
    }
}
