package service.controllers.rest;

import interfaces.IUserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.UserRepository;
import responses.UserResponse;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserManagementController {
    private IUserRepository userRepository = new UserRepository();

    @RequestMapping("/register")
    public UserResponse register(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        return userRepository.register(username, password);
    }

    @RequestMapping("/login")
    public UserResponse login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password){
        return userRepository.login(username, password);
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
