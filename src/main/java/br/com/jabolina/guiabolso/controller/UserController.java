package br.com.jabolina.guiabolso.controller;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}/transacoes/{ano}/{mes}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getTransactionsForUser(@PathVariable("id") Integer id,
                                                                    @PathVariable("ano") Integer year,
                                                                    @PathVariable("mes") Integer month) {
        return ResponseEntity.ok(userService.findTransactionsForPeriod(id, month, year));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
