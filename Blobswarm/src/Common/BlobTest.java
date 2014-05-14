package Common;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class BlobTest {

	@Test
	public void test() {
		Blob BlobTest = new Blob();
		
		Point test = new Point(50,50);
		assertEquals(BlobTest.getPosition(), test);
		
		BlobTest.move(1);
		test.setLocation(50,50 - BlobTest.getSpeed());
		assertEquals(BlobTest.getPosition(), test);
		
		BlobTest.move(2);
		test.setLocation(50,50);
		assertEquals(BlobTest.getPosition(), test);
		
		BlobTest.move(3);
		test.setLocation(50 - BlobTest.getSpeed(),50);
		assertEquals(BlobTest.getPosition(), test);
		
		BlobTest.move(4);
		test.setLocation(50,50);
		assertEquals(BlobTest.getPosition(), test);
		
		BlobTest.move(1337);
		test.setLocation(50,50);
		assertEquals(BlobTest.getPosition(), test);
		
		
		
		assertTrue(BlobTest.contains(test));
		
		test.setLocation(100,50);
		
		assertFalse(BlobTest.contains(test));
	}

}
