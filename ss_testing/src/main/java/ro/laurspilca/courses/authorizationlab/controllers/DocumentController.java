package ro.laurspilca.courses.authorizationlab.controllers;

import ro.laurspilca.courses.authorizationlab.model.CreateDocumentRequest;
import ro.laurspilca.courses.authorizationlab.model.Document;
import ro.laurspilca.courses.authorizationlab.services.DocumentService;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/documents")
    public ResponseEntity<Document> create(@Valid @RequestBody CreateDocumentRequest request,
                                           @AuthenticationPrincipal Jwt jwt) {
        Document document = documentService.create(request, jwt.getSubject());
        return ResponseEntity.created(URI.create("/api/documents/" + document.id())).body(document);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/documents/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable String id) {
        documentService.archive(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/documents")
    public List<Document> findByOwner(@PathVariable String userId) {
        return documentService.findByOwner(userId);
    }

    @GetMapping("/tenants/{tenantId}/documents")
    public List<Document> findByTenant(@PathVariable String tenantId) {
        return documentService.findByTenant(tenantId);
    }
}
