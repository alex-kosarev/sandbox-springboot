package name.alexkosarev.sandbox.springboot.rest.services;

import name.alexkosarev.sandbox.springboot.rest.wrappers.MessageWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleService {

    @RequestMapping
    public MessageWrapper index() {
        return new MessageWrapper("Hello world!");
    }
}
