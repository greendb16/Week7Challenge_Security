package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        if(userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "home";
    }

    @GetMapping("/add")
    public String addMessage(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("message", new Message());
        model.addAttribute("user", userRepository.findByUsername(username));


        return "messageform";
    }

    @PostMapping("/process")
    public String processMessage(@Valid Message message, BindingResult result){
        message.setUser(userService.getUser());
        if(result.hasErrors()){
            return "messageform";
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
        public String passLogin(){
            return "home";
        }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());

        return "messageform";
    }

    @RequestMapping("/detail/{id}")
    public String detailMessage(@PathVariable("id") long id, Model model){
        if(userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        model.addAttribute(messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/details")
    public String userDetails(Model model){
        long userId = userService.getUser().getId();
        model.addAttribute("messages", messageRepository.findByUserId(userId));
        if(userService.getUser() != null) {
            model.addAttribute("currentUser", userService.getUser());
        }
        return "userDetails";
    }
}
