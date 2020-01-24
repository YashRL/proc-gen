package fr.theorozier.procgen.common.util.concurrent;

import java.util.Comparator;
import java.util.concurrent.*;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {
	
	public static final Comparator<Runnable> ASC_COMPARATOR = new Comparator<Runnable>() {
		public int compare(Runnable o1, Runnable o2) {
			
			if (o1 == null) {
				
				if (o2 == null) {
					return 0;
				} else {
					return -1;
				}
				
			} else if (o2 == null) {
				return 1;
			} else {
				return ((PrioritySupplier) o1).getPriority() - ((PrioritySupplier) o2).getPriority();
			}
			
		}
	};
	
	public PriorityThreadPoolExecutor(int poolSize, Comparator<Runnable> comp) {
		super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(11, comp));
	}
	
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		RunnableFuture<T> newTaskFor = super.newTaskFor(callable);
		return new PriorityFuture<>(newTaskFor, ((PrioritySupplier) callable).getPriority());
	}
	
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		RunnableFuture<T> newTaskFor = super.newTaskFor(runnable, value);
		return new PriorityFuture<>(newTaskFor, ((PrioritySupplier) runnable).getPriority());
	}
	
}
