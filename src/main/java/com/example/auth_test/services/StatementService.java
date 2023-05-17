package com.example.auth_test.services;

import com.example.auth_test.exceptions.StatementException;
import com.example.auth_test.model.statement.Statement;
import com.example.auth_test.model.user.User;
import com.example.auth_test.repos.StatementRepository;
import com.example.auth_test.requests.StatementRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final StatementRepository statementRepository;

    public void save(StatementRequest request, User user) {
        Statement statement = Statement.builder()
                .header(request.getHeader())
                .description(request.getDescription())
                .tag(request.getTag())
                .creator(user)
                .date(LocalDateTime.now())
                .image(request.getImage())
                .build();
        statementRepository.save(statement);
    }

    public void update(StatementRequest request, Long id) {
        statementRepository.updateStatement(id, request.getHeader(), request.getDescription(),
                request.getTag(), request.getImage());
    }

    public void delete(Long id) {
        statementRepository.deleteById(id);
    }

    public List<Statement> allStatements() {
        return statementRepository.findAll();
    }

    public Statement getStatementById(Long id) {
        return statementRepository.findById(id).orElseThrow(StatementException::new);
    }

}
