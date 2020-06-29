package fr.theorozier.procgen.common.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegistryOrigin<T> implements Registry<T> {
	
	private final Map<String, T> internal = new HashMap<>();
	private final Map<String, RegistryNamespaced<T>> namespaces = new HashMap<>();
	private boolean frozen = false;
	
	private void checkFrozen() {
		if (this.frozen) {
			throw new IllegalStateException("This registry is frozen.");
		}
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public void freeze() {
		this.setFrozen(true);
	}
	
	@Override
	public void register(String key, T value) {
		
		this.checkFrozen();
		
		if (this.internal.containsKey(key)) {
			throw new IllegalArgumentException("This key is duplicated.");
		}
		
		this.internal.put(key, value);
		
	}
	
	@Override
	public void unregister(String key) {
		this.checkFrozen();
		this.internal.remove(key);
	}
	
	@Override
	public boolean contains(String key) {
		return this.internal.containsKey(key);
	}
	
	@Override
	public T get(String key) {
		return this.internal.get(key);
	}
	
	public Registry<T> namespaced(String namespace) {
		return this.namespaces.computeIfAbsent(namespace, ns -> new RegistryNamespaced<>(this, ns));
	}
	
	public Collection<T> values() {
		return Collections.unmodifiableCollection(this.internal.values());
	}
	
}
