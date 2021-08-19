package com.james.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.james.authentication.models.User;
import com.james.authentication.services.UserService;

@Controller
public class Users {
	private final UserService userService;
	
	public Users(UserService userService) {
		this.userService = userService;
	}
	@RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
		
        return "registrationPage.jsp";
    }
    @RequestMapping("/login")
    public String login() {
        return "loginPage.jsp";
    }
    
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
        if(result.hasErrors()) {
        	return "registrationPage.jsp";
        } else {
        	userService.registerUser(user);
        	return "redirect:/login";
        }
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes flash) {
        // if the user is authenticated, save their user id in session
    	if(userService.authenticateUser(email, password)) {
    		session.setAttribute("uuid", userService.findByEmail(email).getId());
    		return "redirect:/home";
    	} else {
    		flash.addFlashAttribute("error", "Invalid Email or Password!");
    		return "redirect:/login";
    	}
        // else, add error messages and return the login page
    }
    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        // get user from session, save them in the model and return the home page
    	model.addAttribute(userService.findUserById((Long)session.getAttribute("uuid")));
    	return "homePage.jsp";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
    	return "redirect:/login";
        // invalidate session
        // redirect to login page
    }
}
