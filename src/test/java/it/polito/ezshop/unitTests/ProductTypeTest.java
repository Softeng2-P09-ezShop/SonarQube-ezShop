package it.polito.ezshop.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.model.ProductType;

public class ProductTypeTest {

    @Test
    public void testNegativeSetId() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Integer mId = product.getId();
        product.setId(-5);
        assertEquals(mId, product.getId());
        // boundary
        product.setId(0);
        assertEquals(mId, product.getId());
    }

    @Test
    public void testPositiveSetId() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Integer id = 5;
        product.setId(5);
        assertEquals(id, product.getId());
        // boundary
        product.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, product.getId(),1);
    }

    @Test
    public void testNullSetId() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Integer id = product.getId();
        product.setId(null);
        assertEquals(id, product.getId());
    }

    @Test
    public void testNegativeSetQuantity() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Integer quantity = product.getQuantity();
        product.setQuantity(-5);
        assertEquals(quantity, product.getQuantity());
        // boundary
        product.setId(-1);
        assertEquals(quantity, product.getQuantity());
    }

    @Test
    public void testPositiveSetQuantity() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        product.setQuantity(20);
        Integer quantity = 20;
        assertEquals(quantity, product.getQuantity());
        // boundary
        product.setQuantity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, product.getQuantity(),1);
    }

    @Test
    public void testInvalidSetQuantity() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Integer quantity = product.getQuantity();
        product.setQuantity(null);
        assertEquals(quantity, product.getQuantity());
    }

    @Test
    public void testNegativeSetPricePerUnit() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Double price = product.getPricePerUnit();
        product.setPricePerUnit(-0.50);
        assertEquals(price, product.getPricePerUnit());
        // boundary
        product.setPricePerUnit(-0.0001);
        price = product.getPricePerUnit();
        assertEquals(price, product.getPricePerUnit());
    }

    @Test
    public void testInvalidSetPricePerUnit() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        Double price = product.getPricePerUnit();
        product.setPricePerUnit(null);
        assertEquals(price, product.getPricePerUnit());
    }

    @Test
    public void testPositiveSetPricePerUnit() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.50, 50);
        product.setPricePerUnit(0.60);
        Double price = 0.60;
        assertEquals(price, product.getPricePerUnit());
        //boundary
        product.setPricePerUnit(0.0001);
        Double priceunit = 0.0001;
        assertEquals(priceunit, product.getPricePerUnit());
    }

    @Test
    public void testInvalidSetBarCode() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String barcode = product.getBarCode();
        product.setBarCode("");
        assertEquals(barcode, product.getBarCode());
    }

    @Test
    public void testNullSetBarCode() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String barcode = product.getBarCode();
        product.setBarCode(null);
        assertEquals(barcode, product.getBarCode());
    }

    @Test
    public void testPositiveSetBarCode() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        product.setBarCode("78515420");
        assertEquals("78515420", product.getBarCode());
    }

    @Test
    public void testInvalidSetProductDescription() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String description = product.getProductDescription();
        product.setProductDescription("");
        assertEquals(description, product.getProductDescription());
    }

    @Test
    public void testNullSetProductDescription() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String description = product.getProductDescription();
        product.setProductDescription(null);
        assertEquals(description, product.getProductDescription());
    }

    @Test
    public void testPositiveSetProductDescription() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        product.setProductDescription("yellow");
        assertEquals("yellow", product.getProductDescription());
    }

    @Test
    public void testInvalidNote() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String note = product.getNote();
        product.setNote("");
        assertEquals(note, product.getNote());
    }

    @Test
    public void testNullSetNote() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        String note = product.getNote();
        product.setNote(null);
        assertEquals(note, product.getNote());
    }

    @Test
    public void testPositiveSetNote() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        product.setNote("bad");
        assertEquals("bad", product.getNote());
    }

    @Test
    public void testSetLocation() {
        ProductType product = new ProductType(5, "shelves", "good", "red", "010003004", 0.55, 50);
        product.setLocation("hall");
        assertEquals("hall", product.getLocation());
        product.setLocation("");
        assertEquals("", product.getLocation());
        product.setLocation(null);
        assertNull(product.getLocation());
    }
    
    
    @Test
    public void testDBMethods() throws SQLException {
    	
   
//		boolean update = it.polito.ezshop.model.ProductType.updateProductTypeInDb(1, "newDescription", "9330462119319", 4, "newNote");
//		assertTrue(update);


		int id = -1;
		id = it.polito.ezshop.data.model.ProductType.storeProductTypeInDb("p300", "9330462119322", 2, "note");
    	
		List<it.polito.ezshop.data.ProductType> products = new ArrayList<>();
		products = it.polito.ezshop.data.model.ProductType.getAllProductTypesFromDb();
		for(it.polito.ezshop.data.ProductType p : products) {
			assertNotEquals(p, null);
		}
		
		it.polito.ezshop.data.ProductType p = it.polito.ezshop.data.model.ProductType.getProductTypeByBarCodeFromDb("9330462119322");
		assertNotEquals(p, null);
		
		
		List<it.polito.ezshop.data.ProductType> products1 = new ArrayList<>();
		products1 = it.polito.ezshop.data.model.ProductType.getProductTypesByDescriptionFromDb("p300");
		for(it.polito.ezshop.data.ProductType p1 : products1) {
			assertNotEquals(p1, null);
		}
    	     
		boolean update1 = it.polito.ezshop.data.model.ProductType.updateQuantityInDb(p.getId(), 11);
		assertTrue(update1);
		
		boolean update2 = it.polito.ezshop.data.model.ProductType.updatePositionInDb(p.getId(), "123-try-124");
		assertTrue(update2);
			
		boolean update3 = it.polito.ezshop.data.model.ProductType.deleteProductTypeFromDb(p.getId());
		assertTrue(update3);
    }
}