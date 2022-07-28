package com.tpe.controller;

import com.tpe.controller.dto.LoginRequest;
import com.tpe.controller.dto.RegisterRequest;
import com.tpe.domain.User;
import com.tpe.security.JwtUtils;
import com.tpe.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class JWTController {


    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome the Secured Area";
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        User user =new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setUserName(registerRequest.getUserName());
        user.setPassword(registerRequest.getPassword());

        userService.registerUser(user);

        Map<String,String> map=new HashMap<>();
        map.put("message","User registered successfully");
        map.put("success","true");

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

   @PostMapping("/login")
    public ResponseEntity<Map<String,String>> authenticate(@RequestBody LoginRequest loginRequest){

        Authentication authentication= authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));

        String token=jwtUtils.generateJwtToken(authentication);

        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("success","true");

       return new ResponseEntity<>(map,HttpStatus.OK);
   }
}
