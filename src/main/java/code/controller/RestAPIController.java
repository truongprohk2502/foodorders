package code.controller;

import code.model.Account;
import code.service.AccountService;
import code.session.CartSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@SessionAttributes("cart")
public class RestAPIController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    private void loginSecurity(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

    }

    @PostMapping("/loginAPI")
    public ResponseEntity<String> login(@RequestBody Account account, HttpServletRequest request) {
        Account a = accountService.findAccountByUsername(account.getUsername());

        if (a != null) {
            if (a.getPassword().equals(account.getPassword())) {
                loginSecurity(account.getUsername(), account.getPassword(), request);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid-password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid-username");
        }

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account account, HttpServletRequest request) {
        if (accountService.findAccountByUsername(account.getUsername()) == null) {
            Account a = new Account();
            a.setUsername(account.getUsername());
            a.setPassword(account.getPassword());
            a.setRole("ROLE_USER");
            accountService.save(a);
            loginSecurity(account.getUsername(), account.getPassword(), request);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("duplicate-username");
        }
    }

    @GetMapping("/removeCart/{id}")
    public ResponseEntity<Void> removeCart(@PathVariable Long id, @ModelAttribute("cart") CartSession cart) {
        cart.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
