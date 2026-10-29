package ro.laurspilca.courses.authorizationlab.controllers;

import ro.laurspilca.courses.authorizationlab.model.InfoResponse;
import ro.laurspilca.courses.authorizationlab.services.InfoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    private final InfoService infoService;

    public PublicController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/api/public/info")
    public InfoResponse info() {
        return new InfoResponse(infoService.applicationName());
    }
}
