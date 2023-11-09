package com.xiilab.learnwaybatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xiilab.learnwaybatch.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

}
