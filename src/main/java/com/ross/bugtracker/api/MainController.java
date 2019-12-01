package com.ross.bugtracker.api;

import com.ross.bugtracker.model.Bug;
import com.ross.bugtracker.model.Comment;
import com.ross.bugtracker.model.Status;
import com.ross.bugtracker.model.UserRegistrationModel;
import com.ross.bugtracker.repository.BugRepository;
import com.ross.bugtracker.repository.UserDetailsRepository;
import com.ross.bugtracker.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.time.Instant;

@Controller
public class MainController {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final BugRepository bugRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public MainController(CustomAuthenticationProvider customAuthenticationProvider, BugRepository bugRepository, UserDetailsRepository userDetailsRepository) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.bugRepository = bugRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {
        return "index";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorPage(Model model) {
        return "error";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model model) {
        return "register";
    }

    @RequestMapping(value = "/register_check", method = RequestMethod.POST)
    public String registerCheckPage(@ModelAttribute("user") UserRegistrationModel user, Model model) {
        if (user.getPassword().equals(user.getConfirmPassword())) {
            customAuthenticationProvider.register(user.getUsername(), user.getFullname(), user.getRole(), user.getPassword());
            model.addAttribute("success", true);
            return "register";
        }
        model.addAttribute("error", true);
        return "register";
    }

    @RequestMapping(value = "/bugs", method = RequestMethod.GET)
    public String bugsPage(Model model, Principal principal) {
        System.out.println(principal);
        model.addAttribute("bugs", bugRepository.findAll());
        return "bugs";
    }

    @RequestMapping(value = "/bugs/{id}", method = RequestMethod.GET)
    public String bugPage(@PathVariable("id") String bugId, Model model) {
        model.addAttribute("bug", bugRepository.findById(bugId).get());
        return "bug";
    }

    @RequestMapping(value = "/create_bug", method = RequestMethod.POST)
    public String createBug(@ModelAttribute("bug") Bug bug) {
        bugRepository.save(bug);
        return "redirect:/bugs";
    }

    @RequestMapping(value = "/delete_bug", method = RequestMethod.POST)
    public String createBug(@RequestParam("bugId") String bugId) {
        bugRepository.deleteById(bugId);
        return "redirect:/bugs";
    }

    @RequestMapping(value = "/add_comment", method = RequestMethod.POST)
    public String addComment(@RequestParam("bugId") String bugId, @ModelAttribute("comment") Comment comment, Principal principal) {
        Bug bug = bugRepository.findById(bugId).get();
        if (bug.getStatus() != Status.CLOSED) {
            String fullname = userDetailsRepository.findById(principal.getName()).get().getFullName();
            comment.setAuthor(fullname);
            comment.setCreationDate(Date.from(Instant.now()));
            bug.getComments().add(comment);
            bugRepository.save(bug);
        }
        return "redirect:/bug?bugId=" + bugId;
    }

    @RequestMapping(value = "/update_status", method = RequestMethod.POST)
    public String updateStatus(@RequestParam("bugId") String bugId, @ModelAttribute("status") Status status) {
        System.out.println("Updating status for " + bugId + " to " + status.name());
        Bug bug = bugRepository.findById(bugId).get();
        bug.setStatus(status);
        bugRepository.save(bug);
        return "redirect:/bug?bugId=" + bugId;
    }


}
