package employee_management_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Employee Management API Running";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/version")
    public String version() {
        return "Version 1.0";
    }
}
