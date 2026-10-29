package ro.laurspilca.courses.authorizationlab.controllers;

import ro.laurspilca.courses.authorizationlab.model.AuditResponse;
import ro.laurspilca.courses.authorizationlab.services.AuditService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final AuditService auditService;

    public AdminController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/api/admin/audit")
    public AuditResponse audit() {
        return new AuditResponse(auditService.eventCount());
    }
}
