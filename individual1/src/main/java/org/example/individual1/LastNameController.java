package org.example.individual1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/last_name_controller")
public class LastNameController {
    @GetMapping
  public String getLastName() {

        return "<h1>Kotulska</h1>";
    }
}

