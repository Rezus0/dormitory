package com.example.auth_test.repos;

import com.example.auth_test.model.statement.Block;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByNumber(String number);
    @Modifying
    @Transactional
    @Query(
            "update Block b set b.number = '123456' where b.number = :number"
    )
    void updateStudents(@Param("number") String number);
}
