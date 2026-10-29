package ro.laurspilca.courses.authorizationlab.repositories;

import ro.laurspilca.courses.authorizationlab.model.Document;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.springframework.stereotype.Repository;

@Repository
public class DocumentRepository {

    private static final Comparator<String> ID_ORDER =
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());

    private static final int FIRST_GENERATED_ID = 103;

    private final Map<String, Document> documents = new TreeMap<>(ID_ORDER);
    private int nextId;

    public DocumentRepository() {
        reset();
    }

    public synchronized void reset() {
        documents.clear();
        store(new Document("d-101", "Spring notes", "ana", "alpha", false));
        store(new Document("d-102", "Test strategy", "bogdan", "beta", false));
        nextId = FIRST_GENERATED_ID;
    }

    public synchronized Optional<Document> findById(String id) {
        return Optional.ofNullable(documents.get(id));
    }

    public synchronized List<Document> findAll() {
        return List.copyOf(documents.values());
    }

    public synchronized List<Document> findByOwner(String owner) {
        return filter(document -> document.owner().equals(owner));
    }

    public synchronized List<Document> findByTenant(String tenant) {
        return filter(document -> document.tenant().equals(tenant));
    }

    public synchronized long count() {
        return documents.size();
    }

    public synchronized Document create(String title, String owner, String tenant) {
        return store(new Document("d-" + nextId++, title, owner, tenant, false));
    }

    public synchronized Document save(Document document) {
        return store(document);
    }

    public synchronized boolean deleteById(String id) {
        return documents.remove(id) != null;
    }

    private Document store(Document document) {
        documents.put(document.id(), document);
        return document;
    }

    private List<Document> filter(Predicate<Document> predicate) {
        return documents.values().stream().filter(predicate).toList();
    }
}
