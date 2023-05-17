package com.example.auth_test.endpoints;

import com.example.auth_test.model.statement.Statement;
import com.example.auth_test.model.user.User;
import com.example.auth_test.requests.StatementRequest;
import com.example.auth_test.services.CookieService;
import com.example.auth_test.services.StatementService;
import com.example.auth_test.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statement")
@RequiredArgsConstructor
public class StatementEndpoint {
    private final UserService userService;
    private final StatementService statementService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> addStatement(@RequestBody @Valid StatementRequest request,
                                               HttpServletRequest httpRequest) {
        User user = userService.extractUserFromRequest(httpRequest);
        statementService.save(request, user);
        return ResponseEntity.ok("Statement has been successfully added");
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> editStatement(@RequestBody @Valid StatementRequest request,
                                                @PathVariable(name = "id") Long id) {
        statementService.update(request, id);
        return ResponseEntity.ok("Statement has been successfully edited");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> deleteStatement(@PathVariable(name = "id") Long id) {
        statementService.delete(id);
        return ResponseEntity.ok("Statement has been successfully deleted");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Statement>> allStatements() {
        return ResponseEntity.ok(statementService.allStatements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statement> showStatement(@PathVariable("id") Long id) {
        return ResponseEntity.ok(statementService.getStatementById(id));
    }

}
