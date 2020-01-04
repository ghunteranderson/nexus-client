package com.ghunteranderson.nexus.maven.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Injector{
	private final DependencyProvider provider;
	
	public void inject(MojoContext context, Object obj) {
		List<Field> fields = getAllFields(obj.getClass());
	
		for(Field field : fields) {
			if(field.isAnnotationPresent(Dependency.class)) {
				field.setAccessible(true);
				try {
					field.set(obj, provider.getInstance(field.getType(), context));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					StringBuilder error = new StringBuilder();
					error.append("Could not set ");
					error.append(obj.getClass().getName());
					error.append('[').append(field.getName()).append("]. ");
					error.append(e.getMessage());
					throw new IllegalStateException(error.toString()); 
				}
			}
		}
		
		
	}
	
	private List<Field> getAllFields(Class<?> clazz){
		Field[] declaredFields = clazz.getDeclaredFields();
		List<Field> out = new ArrayList<>(Arrays.asList(declaredFields));
		if(clazz.getSuperclass() != null)
			out.addAll(getAllFields(clazz.getSuperclass()));
		return out;
	}

}
