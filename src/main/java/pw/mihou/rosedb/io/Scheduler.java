package pw.mihou.rosedb.io;

import java.util.concurrent.*;

public class Scheduler {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE_TIME = 120;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public static final ExecutorService executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new SynchronousQueue<>(),
            new ThreadFactory("RoseDB - Executor - %d", false));

    public static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(CORE_POOL_SIZE, new ThreadFactory("RoseDB - Scheduler - %d", false));

    public static ScheduledExecutorService getScheduler() {
        return scheduledExecutorService;
    }

    public static ScheduledFuture<?> schedule(Runnable task, long delay, long time, TimeUnit measurement) {
        return scheduledExecutorService.scheduleAtFixedRate(task, delay, time, measurement);
    }

    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit measurement) {
        return scheduledExecutorService.schedule(task, delay, measurement);
    }

    public static CompletableFuture<Void> submit(Runnable task) {
        return CompletableFuture.runAsync(task, executorService);
    }

}
