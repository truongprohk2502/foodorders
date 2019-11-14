package code.controller;

import code.model.*;
import code.model.rest.CartInfo;
import code.model.rest.LoginAccount;
import code.service.AccountService;
import code.service.FoodService;
import code.service.OrderedService;
import code.service.SingleOrderService;
import code.session.CartSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@RestController
@SessionAttributes("cart")
public class RestAPIController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderedService orderedService;

    @Autowired
    private JavaMailSender javaMailSender;

    private void loginSecurity(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

    }

    @PostMapping("/loginAPI")
    public ResponseEntity<String> login(@RequestBody LoginAccount loginAccount, HttpServletRequest request) {
        Account account;
        if (loginAccount.getUsernameOrEmail().contains("@")) {
            account = accountService.findAccountByEmail(loginAccount.getUsernameOrEmail());
        } else {
            account = accountService.findAccountByUsername(loginAccount.getUsernameOrEmail());
        }

        if (account != null) {
            if (account.getPassword().equals(loginAccount.getPassword())) {
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
            if (accountService.findAccountByEmail(account.getEmail()) == null) {
                account.setRole("ROLE_USER");
                accountService.save(account);
                loginSecurity(account.getUsername(), account.getPassword(), request);
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("duplicate-email");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("duplicate-username");
        }
    }

    @GetMapping("/removeCart/{id}")
    public ResponseEntity<Void> removeCart(@PathVariable Long id, @ModelAttribute("cart") CartSession cart) {
        cart.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/addToCart/{id}/{qty}")
    public ResponseEntity<SingleOrder> addToCart(@PathVariable Long id, @PathVariable Long qty, @ModelAttribute("cart") CartSession cart) {
        Food food = foodService.findById(id);
        if (food != null) {
            if (!cart.constains(food)) {
                SingleOrder singleOrder = new SingleOrder(food, qty);
                cart.add(singleOrder);
                return new ResponseEntity<>(singleOrder, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getCartInfo")
    public ResponseEntity<CartInfo> getCartInfo(@ModelAttribute("cart") CartSession cart) {
        CartInfo info = new CartInfo(cart.getSubTotal(), cart.getShippingFee(), cart.getTotal());
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PostMapping("/updateCart")
    public ResponseEntity<Void> updateCart(@RequestBody List<SingleOrder> orders, @ModelAttribute("cart") CartSession cart) {
        for (SingleOrder order : orders) {
            if (order.getRemove()) {
                cart.removeById(order.getFoodId());
            } else {
                cart.setQtyById(order.getFoodId(), order.getQuantity());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<Void> updateInfo(Principal principal, @RequestBody Account account) {
        if (principal != null && principal.getName().equals(account.getUsername())) {
            Account find = accountService.findAccountByUsername(account.getUsername());
            find.setFirstName(account.getFirstName());
            find.setLastName(account.getLastName());
            find.setStreetAddress(account.getStreetAddress());
            find.setApartmentNumber(account.getApartmentNumber());
            find.setCity(account.getCity());
            find.setDistrict(account.getDistrict());
            find.setImage(account.getImage());
            find.setPhone(account.getPhone());
            accountService.save(find);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/order")
    public ResponseEntity<Void> orderFood(@RequestBody Account info, Principal principal, @ModelAttribute("cart") CartSession cart) {
        Ordered ordered = new Ordered();
        ordered.setStatus("DELIVERING");
        ordered.setSingleOrders(cart.getSingleOrders());
        ordered.setOrderDate(new Timestamp(System.currentTimeMillis()));

        for (SingleOrder order : cart.getSingleOrders()) {
            order.setOrdered(ordered);
        }

        if (principal != null) {
            Account account = accountService.findAccountByUsername(principal.getName());
            if (account != null) {
                ordered.setAccount(account);
                orderedService.save(ordered);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            info.setRole("ROLE_GUEST");
            accountService.save(info);
            ordered.setAccount(info);
            orderedService.save(ordered);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody Feedback feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(Feedback.TARGET_EMAIL);
        message.setFrom(Feedback.FROM_EMAIL);
        message.setSubject(Feedback.SUBJECT_EMAIL);
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText("Customer Name: " + feedback.getName() + "\n"
                + "Email: " + feedback.getEmail() + "\n"
                + "Phone: " + feedback.getPhone() + "\n"
                + "Content: " + feedback.getContent() + "\n"
                + "Sending Date: " + message.getSentDate());
        javaMailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/deleteFood/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id, Principal principal) {
        if (principal != null) {
            Account account = accountService.findAccountByUsername(principal.getName());
            if (account != null && "ROLE_ADMIN".equals(account.getRole())) {
                foodService.deleteFoodById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/admin/saveFood")
    public ResponseEntity<Void> createFood(@RequestBody Food food, Principal principal) {
        if (principal != null) {
            Account account = accountService.findAccountByUsername(principal.getName());
            if (account != null && "ROLE_ADMIN".equals(account.getRole())) {
                foodService.save(food);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/admin/finishOrder/{type}/{id}")
    public ResponseEntity<Void> finishOrder(@PathVariable String type, @PathVariable Long id, Principal principal) {
        if (principal != null) {
            Account account = accountService.findAccountByUsername(principal.getName());
            if (account != null && "ROLE_ADMIN".equals(account.getRole())) {
                Ordered ordered = orderedService.findById(id);
                if ("delivered".equals(type)) {
                    ordered.setStatus("DELIVERED");
                } else if ("canceled".equals(type)) {
                    ordered.setStatus("CANCELED");
                }
                orderedService.save(ordered);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
