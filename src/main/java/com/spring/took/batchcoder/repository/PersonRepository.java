package com.spring.took.batchcoder.repository;


import com.spring.took.batchcoder.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
