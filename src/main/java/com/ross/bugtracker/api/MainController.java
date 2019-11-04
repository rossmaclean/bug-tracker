package com.ross.bugtracker.api;

import com.ross.bugtracker.model.Bug;
import com.ross.bugtracker.model.Comment;
import com.ross.bugtracker.model.Status;
import com.ross.bugtracker.model.UserRegistrationModel;
import com.ross.bugtracker.repository.BugRepository;
import com.ross.bugtracker.repository.UserCredentialRepository;
import com.ross.bugtracker.service.BugService;
import com.ross.bugtracker.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private BugRepository bugRepository;

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
            customAuthenticationProvider.register(user.getUsername(), user.getFullname(), user.getPassword());
            return "register";
        }
        model.addAttribute("error", true);
        return "register";
    }

    @Autowired
    private BugService bugService;

    @RequestMapping(value = "/bugs", method = RequestMethod.GET)
    public String bugsPage(Model model, Principal principal) {
        System.out.println(principal);
        model.addAttribute("bugs", bugRepository.findAll());
        return "bugs";
    }

    @RequestMapping(value = "/bugs/{id}", method = RequestMethod.GET)
    public String bugaPage(@PathVariable("id") String bugId, Model model, Principal principal) {
        model.addAttribute("bug", bugRepository.findById(bugId.toString()).get());
        return "bug";
    }

    @RequestMapping(value = "/create_bug", method = RequestMethod.POST)
    public String createBug(@ModelAttribute("bug") Bug bug, Model model, Principal principal) {
        bugRepository.save(bug);
        return "redirect:/bugs";
    }

    @RequestMapping(value = "/bug", method = RequestMethod.GET)
    public String bugPage(@RequestParam("bugId") String bugId, Model model, Principal principal) {
        model.addAttribute("bug", bugRepository.findById(bugId).get());
        return "bug";
    }

    @RequestMapping(value = "/delete_bug", method = RequestMethod.POST)
    public String createBug(@RequestParam("bugId") String bugId, Model model, Principal principal) {
        bugRepository.deleteById(bugId);
        return "redirect:/bugs";
    }

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @RequestMapping(value = "/add_comment", method = RequestMethod.POST)
    public String addComment(@RequestParam("bugId") String bugId, @ModelAttribute("comment") Comment comment, Principal principal) {
        Bug bug = bugRepository.findById(bugId).get();
        if (bug.getStatus() != Status.CLOSED) {
            String fullname = userCredentialRepository.findById(principal.getName()).get().getFullName();
            comment.setAuthor(fullname);
            comment.setCreationDate(Date.from(Instant.now()));
            bug.getComments().add(comment);
            bugRepository.save(bug);
        }
        return "redirect:/bug?bugId=" + bugId;
    }

    @RequestMapping(value = "/update_status", method = RequestMethod.POST)
    public String updateStatus(@RequestParam("bugId") String bugId, @ModelAttribute("status") Status status, Model model, Principal principal) {
        System.out.println("Updating status for " + bugId + " to " + status.name());
        Bug bug = bugRepository.findById(bugId).get();
        bug.setStatus(status);
        bugRepository.save(bug);
        return "redirect:/bug?bugId=" + bugId;
    }


}
