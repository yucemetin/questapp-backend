package com.example.questapp.controllers;

import com.example.questapp.business.abstracts.UserService;
import com.example.questapp.business.requests.CreateUserRequest;
import com.example.questapp.business.requests.UpdateUserRequest;
import com.example.questapp.business.responses.user.CreateUserResponse;
import com.example.questapp.business.responses.user.GetAllUsersResponse;
import com.example.questapp.business.responses.user.GetUserByIdResponse;
import com.example.questapp.business.responses.user.UpdateUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private UserService userService;

    @GetMapping
    public List<GetAllUsersResponse> getAll() {
        return this.userService.getAll();
    }

    @GetMapping("/{id}")
    public GetUserByIdResponse getById(@PathVariable Long id) {
        return this.userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        return this.userService.createUser(createUserRequest);
    }

    @PutMapping
    public UpdateUserResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable Long id) {
        return this.userService.updateUser(updateUserRequest, id);
    }

    @DeleteMapping("/{id}")
    public Long deleteUser(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }

}
