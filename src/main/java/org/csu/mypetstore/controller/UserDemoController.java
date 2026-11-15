package org.csu.mypetstore.controller;

import org.csu.mypetstore.dto.UserReadResult;
import org.csu.mypetstore.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@Profile("dev")
public class UserDemoController {

    private final UserService userService;

    public UserDemoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/demo/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserReadResult> findUser(@RequestParam(name = "username", defaultValue = "alice") String username) {
        return ResponseEntity.of(userService.findByUsername(username));
    }

    @GetMapping(value = "/demo/user/page", produces = MediaType.TEXT_HTML_VALUE)
    public String showUserPage(@RequestParam(name = "username", defaultValue = "alice") String username, Model model) {
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("message", "Thyme leaf 初始化成功!");
        model.addAttribute("user", user);
        model.addAttribute("username", username);
        return "demo/user";
    }
}
