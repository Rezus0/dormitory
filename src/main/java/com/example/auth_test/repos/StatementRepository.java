package com.example.auth_test.repos;

import com.example.auth_test.model.statement.Statement;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    @Transactional
    @Modifying
    @Query(
            "update Statement s set s.header = :header," +
                    " s.description = :description," +
                    " s.tag = :tag," +
                    " s.image = :image where s.id = :id"
    )
    void updateStatement(@Param("id") Long id,
                                @Param("header") String header,
                                @Param("description") String description,
                                @Param("tag") String tag,
                                @Param("image") String image);
}
