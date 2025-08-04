package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() throws IdInValidException {

        if(true)
            throw new IdInValidException("check helllllo");
        return "Phan Thi Hong Hue";
    }
}
