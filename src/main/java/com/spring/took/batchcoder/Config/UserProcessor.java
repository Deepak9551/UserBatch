package com.spring.took.batchcoder.Config;

import com.spring.took.batchcoder.Entity.User;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User,User> {
    @Override
    public User process(User item) throws Exception {
        item.setFirstName(item.getFirstName().toUpperCase());
        item.setLastName(item.getLastName().toUpperCase());
        return null;
    }
}
