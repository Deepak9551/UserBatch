package com.spring.took.batchcoder.Reposistory;

import com.spring.took.batchcoder.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJobRepo  extends JpaRepository<User,Long> {

}
