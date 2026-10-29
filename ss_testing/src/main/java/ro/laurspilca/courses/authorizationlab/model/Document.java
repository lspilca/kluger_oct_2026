package ro.laurspilca.courses.authorizationlab.model;

public record Document(String id, String title, String owner, String tenant, boolean archived) {

    public Document withArchived(boolean archived) {
        return new Document(id, title, owner, tenant, archived);
    }
}
