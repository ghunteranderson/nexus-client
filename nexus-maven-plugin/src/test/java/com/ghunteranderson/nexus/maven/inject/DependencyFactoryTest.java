package com.ghunteranderson.nexus.maven.inject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Test;

public class DependencyFactoryTest {

	@Test
	public void getInstance_sameInstanceForSameContext(){
		MojoContext context1 = new MojoContext();
		MojoContext context2 = new MojoContext();
		DependencyFactory factory = new DependencyFactory();
		factory.bind(Properties.class, (c, f) -> {
			assertTrue(f == factory);
			assertNotNull(c);
			return new Properties();
		});
		
		Properties p1 = factory.getInstance(Properties.class, context1);
		Properties p2 = factory.getInstance(Properties.class, context1);
		Properties p3 = factory.getInstance(Properties.class, context2);
		
		assertTrue(p1 == p2);
		assertTrue(p2 != p3);
	}
}
