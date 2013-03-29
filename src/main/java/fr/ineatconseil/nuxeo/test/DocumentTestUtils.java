package fr.ineatconseil.nuxeo.test;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

public abstract class DocumentTestUtils {

    public abstract CoreSession getSession();

    public DocumentModel create(String path, String name, String type)
            throws ClientException {
        DocumentModel doc = build(path, name, type);
        doc = getSession().createDocument(doc);
        doc = getSession().saveDocument(doc);
        getSession().save();
        return doc;
    }

    public DocumentModel create(DocumentModel parent, String name, String type)
            throws ClientException {
        return create(parent.getPathAsString(), name, type);
    }

    public DocumentModel build(String path, String name, String type)
            throws ClientException {
        DocumentModel doc = getSession().createDocumentModel(path, name, type);
        doc.setPropertyValue("dc:title", name);
        return doc;
    }

    // si cleanup granularity.method marche pas !
    public void resetWorkspace() throws ClientException {
        DocumentModel root = getSession().getDocument(new PathRef("/"));
        for (DocumentModel doc : getSession().getChildren(root.getRef())) {
            deleteInCascade(doc);
        }
    }

    public void deleteInCascade(DocumentModel document) throws ClientException {
        for (DocumentModel doc : getSession().getChildren(document.getRef())) {
            deleteInCascade(doc);
        }
        getSession().removeDocument(document.getRef());
    }

}
