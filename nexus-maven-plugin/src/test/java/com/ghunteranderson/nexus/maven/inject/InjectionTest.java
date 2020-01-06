package com.ghunteranderson.nexus.maven.inject;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ghunteranderson.nexus.maven.inject.Dependency;
import com.ghunteranderson.nexus.maven.inject.DependencyFactory;
import com.ghunteranderson.nexus.maven.inject.Injector;
import com.ghunteranderson.nexus.maven.inject.MojoContext;

import lombok.Getter;

public class InjectionTest {

	@Mock private DependencyFactory factory;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void inject_successfulyInjectIntoClass() {
		Injector injector = new Injector(factory);
		MojoContext context = new MojoContext();
		Mockito.when(factory.getInstance(String.class, context)).thenReturn("abc");
		Mockito.when(factory.getInstance(Integer.class, context)).thenReturn(123);
		Mockito.when(factory.getInstance(Character.class, context)).thenReturn('a');
		Mockito.when(factory.getInstance(BigDecimal.class, context)).thenReturn(new BigDecimal("1.0"));
		
		TestClass testClass = new TestClass();
		injector.inject(context, testClass);
		
		assertEquals("abc", testClass.getString());
		assertEquals(123, testClass.getInteger());
		assertEquals('a', testClass.getCharacter());
		assertNull(testClass.getBigDecimal());
	}
	
	@Test
	public void inject_successfulyInjectIntoSuperClass() {
		Injector injector = new Injector(factory);
		MojoContext context = new MojoContext();
		Mockito.when(factory.getInstance(String.class, context)).thenReturn("abc");
		Mockito.when(factory.getInstance(Integer.class, context)).thenReturn(123);
		Mockito.when(factory.getInstance(Character.class, context)).thenReturn('a');
		Mockito.when(factory.getInstance(BigDecimal.class, context)).thenReturn(new BigDecimal("1.0"));
		Mockito.when(factory.getInstance(Double.class, context)).thenReturn(Double.valueOf(23d));
		
		TestClass2 testClass = new TestClass2();
		injector.inject(context, testClass);
		
		assertEquals("abc", testClass.getString());
		assertEquals(123, testClass.getInteger());
		assertEquals('a', testClass.getCharacter());
		assertNull(testClass.getBigDecimal());
		assertEquals(23d, testClass.getNum1());
		assertNull(testClass.getNum2());
	}
	

	@Getter
	private static class TestClass {
		@Dependency private String string;
		@Dependency private Integer integer;
		@Dependency private Character character;
		private BigDecimal bigDecimal;
	}
	
	@Getter
	private static class TestClass2 extends TestClass {
		@Dependency private Double num1;
		private Float num2;
	}
}
