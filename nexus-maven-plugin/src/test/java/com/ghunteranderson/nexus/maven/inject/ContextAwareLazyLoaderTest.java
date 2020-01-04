package com.ghunteranderson.nexus.maven.inject;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContextAwareLazyLoaderTest {

	@Test
	public void getValue_sameValueForSameContext() {
		
		MojoContext c1 = new MojoContext();
		MojoContext c2 = new MojoContext();
		
		
		ContextAwareLazyLoader<Properties> loader = new ContextAwareLazyLoader<>(c -> {
			return new Properties();
		});
		
		Properties c1i1 = loader.getValue(c1);
		Properties c2i1 = loader.getValue(c2);
		Properties c1i2 = loader.getValue(c1);
		Properties c2i2 = loader.getValue(c2);
		
		assertTrue(c1i1 == c1i2); // Context 1 always retrieved the same properties
		assertTrue(c2i1 == c2i2); // Context 2 always retrieved the same properties
		assertTrue(c1i1 != c2i1); // Context 1 and Context 2 have different properties
	}
}
