package de.hsrm.mi.team3.swtp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** use to start backend */
@Controller
@SpringBootApplication
public class SwtpApplication implements ErrorController {

  public static void main(String[] args) {
    SpringApplication.run(SwtpApplication.class, args);
  }

  @RequestMapping(value = "/error")
  public String error() {
    return "forward:/index.html";
  }
}
