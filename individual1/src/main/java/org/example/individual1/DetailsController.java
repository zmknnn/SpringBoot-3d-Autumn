package org.example.individual1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details_controller")
public class DetailsController {
    @GetMapping
    public String getDetails() {
        return "<h1>"
                + "OS: " + System.getProperty("os.name") + "<br>"
                + "Architecture: " + System.getProperty("os.arch") + "<br>"
                + "Processors: " + Runtime.getRuntime().availableProcessors() + "<br>"
                + "Total Memory: " + Runtime.getRuntime().totalMemory() + "<br>"
                + "Free Memory: " + Runtime.getRuntime().freeMemory()
                + "</h1>";
    }
}


