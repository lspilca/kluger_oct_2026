package ro.laurspilca.courses.authorizationlab.services;

import ro.laurspilca.courses.authorizationlab.model.CreateDocumentRequest;
import ro.laurspilca.courses.authorizationlab.model.Document;
import ro.laurspilca.courses.authorizationlab.repositories.DocumentRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document create(CreateDocumentRequest request, String owner) {
        return documentRepository.create(request.title(), owner, request.tenant());
    }

    public void delete(String id) {
        if (!documentRepository.deleteById(id)) {
            throw notFound(id);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_documents.write') and @documentPolicy.isOwner(#id, authentication)")
    public void archive(String id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> notFound(id));
        documentRepository.save(document.withArchived(true));
    }

    @PreAuthorize("hasAuthority('SCOPE_documents.read') and @documentPolicy.isSubject(#userId, authentication)")
    public List<Document> findByOwner(String userId) {
        return documentRepository.findByOwner(userId);
    }

    @PreAuthorize("hasAuthority('SCOPE_documents.read') and @documentPolicy.hasTenant(#tenantId, authentication)")
    public List<Document> findByTenant(String tenantId) {
        return documentRepository.findByTenant(tenantId);
    }

    private ResponseStatusException notFound(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Document " + id + " not found");
    }
}
