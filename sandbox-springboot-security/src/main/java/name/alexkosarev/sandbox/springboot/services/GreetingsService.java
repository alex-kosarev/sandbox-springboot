package name.alexkosarev.sandbox.springboot.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingsService {
    
    @RequestMapping
    public String greetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "G'day, " + authentication.getName();
    }
}
