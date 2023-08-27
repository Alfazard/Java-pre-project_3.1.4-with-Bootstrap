package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.security.Principal;

/**
 * @author Alfazard on 08.07.2023
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping()
    public String adminPage(Model model, Model role, Principal principal){
        role.addAttribute("rolesList", roleService.getRolesList());
        model.addAttribute("users", userService.showAllUsers());
        User princ = userService.getUserByUsername(principal.getName());
        model.addAttribute("princ", princ);
        model.addAttribute("newUser", new User());
        return "admin-page";
    }

    @GetMapping("/user")
    public String userPageInfo(Model model, Principal principal) {
        model.addAttribute("user",userService.getUserByUsername(principal.getName()));
        return "/user";
    }

    @GetMapping("/user/{id}")
    public String userPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "/user";
    }

    @GetMapping("/new")
    public String addUser(Model model, Model role) {
        role.addAttribute("rolesList", roleService.getRolesList());
        model.addAttribute("user", new User());
        return "/new";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("user") User user){
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String updateUser(Model model, @PathVariable("id") Long id, Model role) {
        role.addAttribute("rolesList", roleService.getRolesList());
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        user.setPassword(null);
        return "/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id) {
        userService.editUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/admin";
    }

}
