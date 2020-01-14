package service.controllers.rest;

import interfaces.IUserRepository;
import org.springframework.web.bind.annotation.*;
import repositories.UserRepository;
import responses.UserResponse;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserManagementController {
    private IUserRepository userRepository = new UserRepository();

    @PostMapping(value = "/register")
    public UserResponse register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        return userRepository.register(username, password);
    }

    @GetMapping(value = "/login")
    public UserResponse login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        return userRepository.login(username, password);
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}