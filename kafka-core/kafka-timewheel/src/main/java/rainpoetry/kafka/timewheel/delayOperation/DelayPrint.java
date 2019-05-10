package rainpoetry.kafka.timewheel.delayOperation;



import rainpoetry.kafka.timewheel.delay.DelayedOperation;
import rainpoetry.kafka.timewheel.delay.DelayedOperationPurgatory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * User: chenchong
 * Date: 2019/1/24
 * description:
 */
public class DelayPrint extends DelayedOperation {

	boolean completable = false;

	public DelayPrint(long delayMs) {
		super(delayMs);
	}

	@Override
	public void onExpiration() {
		info(" expiration operation");
	}

	@Override
	public void onComplete() {
		info("need to complete the operation");
	}

	@Override
	public boolean tryComplete() {
		if (completable)
			return forceComplete();
		return false;
	}


	public static void main(String[] args){
		DelayedOperationPurgatory<DelayedOperation> delayPrintPurgatory = new DelayedOperationPurgatory<>("print");
		Instant start = Instant.now();
		for(int i=0;i<50000;i++) {
			DelayPrint print = new DelayPrint(5000+i);
			delayPrintPurgatory.tryCompleteElseWatch(print, Arrays.asList("demo"+i));
		}
		Instant end = Instant.now();
		System.out.println(Duration.between(start,end).toMillis());
	}
}
