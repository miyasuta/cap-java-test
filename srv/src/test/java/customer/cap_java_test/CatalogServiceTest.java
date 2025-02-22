package customer.cap_java_test;

import static cds.gen.catalogservice.CatalogService_.BOOKS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sap.cds.ql.Delete;
import com.sap.cds.ql.Insert;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.catalogservice.Books;
import cds.gen.catalogservice.CatalogService;
import cds.gen.catalogservice.SubmitOrderContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class CatalogServiceTest {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private PersistenceService db;
    

    @AfterEach
    public void cleanup() {
        db.run(Delete.from(BOOKS));
    }

    @Test
    public void submitOrder() {
        //fill test data
        Books book = Books.create();
        book.setId("1");
        book.setStock(100);
        db.run(Insert.into(BOOKS).entry(book));

        SubmitOrderContext context = SubmitOrderContext.create();
        context.setBook("1");
        context.setQuantity(10);
        catalogService.emit(context);

        assertEquals(90, context.getResult().getStock());
    }
}
