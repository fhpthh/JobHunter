package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.service.error.IdInValidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() throws IdInValidException {

        if(true)
            throw new IdInValidException("check helllllo");
        return "Phan Thi Hong Hue";
    }
}
