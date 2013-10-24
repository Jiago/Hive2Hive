package org.hive2hive.core.test;

import static org.junit.Assert.fail;

public class H2HWaiter {
	private int counter = 0;
	private final int maxAmoutOfTicks;

	public H2HWaiter(int anAmountOfAcceptableTicks) {
		maxAmoutOfTicks = anAmountOfAcceptableTicks;
	}

	public void tickASecond() {
		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
			}
		}
		counter++;
		if (counter >= maxAmoutOfTicks) {
			fail(String.format("We waited for %d seconds. This is simply to long!", counter));
		}
	}
}