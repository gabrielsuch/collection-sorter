package com.gabrielsuch.collectionsorter.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gabrielsuch.collectionsorter.domain.testdouble.Car;
import com.gabrielsuch.collectionsorter.infra.exception.OrderException;
import com.gabrielsuch.collectionsorter.service.impl.CollectionSorterImpl;

public class CollectionSorterTest {
	
	private final Collection<Car> collection = new HashSet<Car>();
	
	@Before
	public void setup() {
		collection.add(new Car("Ford Fiesta", "Blue", 2011, 50000.0));
		collection.add(new Car("Ford Focus", "Silver", 2009, 40000.0));
		collection.add(new Car("Audi A3", "White", 2010, 90000.0));
		collection.add(new Car("Audi A3", "Black", 2010, 99000.0));
		collection.add(new Car("Audi A3", "Black", 2010, 100000.0));
		collection.add(new Car("Audi A3", "Yellow", 2009, 95000.0));
	}
	
	@Test
	public void testSortAnEmptyCollection() {
		OrderCriteria orderCriteria = new OrderCriteria(new Order("model"));
		List<Car> current = new CollectionSorterImpl<Car>(new HashSet<Car>()).sortBy(orderCriteria);
		
		assertTrue(current.isEmpty());
	}
	
	@Test
	public void testOrderBySingleField() {
		OrderCriteria orderCriteria = new OrderCriteria(new Order("model", SortOrder.DESC));
		List<Car> current = new CollectionSorterImpl<Car>(collection).sortBy(orderCriteria);
		
		assertEquals(current.get(0).getModel(), "Ford Focus");
		assertEquals(current.get(1).getModel(), "Ford Fiesta");
		assertEquals(current.get(2).getModel(), "Audi A3");
	}
	
	@Test
	public void testOrderByTwoFields() {
		OrderCriteria orderCriteria = new OrderCriteria(
				new Order("model"),
				new Order("year", SortOrder.DESC)
		);
		List<Car> current = new CollectionSorterImpl<Car>(collection).sortBy(orderCriteria);
		
		assertEquals(current.get(0).getYear(), 2010);
		assertEquals(current.get(1).getYear(), 2010);
		assertEquals(current.get(2).getYear(), 2010);
		assertEquals(current.get(3).getYear(), 2009);
		assertEquals(current.get(4).getYear(), 2011);
		assertEquals(current.get(5).getYear(), 2009);
	}
	
	@Test
	public void testOrderByThreeFields() {
		OrderCriteria orderCriteria = new OrderCriteria(
				new Order("model"),
				new Order("color"),
				new Order("price")
		);
		List<Car> current = new CollectionSorterImpl<Car>(collection).sortBy(orderCriteria);
		
		assertEquals(current.get(0).getColor(), "Black");
		assertEquals(current.get(0).getPrice().intValue(), 99000);
		
		assertEquals(current.get(1).getColor(), "Black");
		assertEquals(current.get(1).getPrice().intValue(), 100000);
		
		assertEquals(current.get(2).getColor(), "White");
	}
	
	@Test(expected = OrderException.class)
	public void testSanityCheck() {
		OrderCriteria orderCriteria = new OrderCriteria(new Order("inexistentField"));
		new CollectionSorterImpl<Car>(collection).sortBy(orderCriteria);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullCollection() {
		OrderCriteria orderCriteria = new OrderCriteria(new Order("inexistentField"));
		new CollectionSorterImpl<Car>(null).sortBy(orderCriteria);
	}
	
}
