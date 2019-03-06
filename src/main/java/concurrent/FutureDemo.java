package concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * User: chenchong
 * Date: 2019/2/20
 * description:
 */
public class FutureDemo {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		 Boolean b = CompletableFuture.supplyAsync(()->{
			 System.out.println("cal");
			 if (true) {
				 throw new IllegalArgumentException("sasas");
			 }
			 return true;
		 }).whenCompleteAsync((k,v)->{
			 System.out.println("k:"+k);
			 System.out.println(v);
		 }).exceptionally(exception->{
		 	exception.printStackTrace();
		 	return false;
		 }).get();

		System.out.println("boolean: " + b);
	}


}
