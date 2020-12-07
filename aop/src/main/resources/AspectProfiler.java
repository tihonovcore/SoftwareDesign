package ###PACKAGE;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;

@Aspect
public class AspectProfiler {
    @Around("execution(* *(..)) && !within(###PACKAGE.AspectProfiler)")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature method = joinPoint.getSignature();
        called(method);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long end = System.currentTimeMillis();
        time(method, end - start);

        return result;
    }

    private void called(Signature method) throws IOException {
        callCount.putIfAbsent(method, 0);

        int old = callCount.get(method);
        callCount.put(method, old + 1);

        render();
    }

    private void time(Signature method, long time) throws IOException {
        timeCount.putIfAbsent(method, 0L);

        long old = timeCount.get(method);
        timeCount.put(method, old + time);

        render();
    }

    static final HashMap<Signature, Integer> callCount = new HashMap<>();
    static final HashMap<Signature, Long> timeCount = new HashMap<>();

    static void render() throws IOException {
        StringBuilder output = new StringBuilder();
        for (Signature s : bank.AspectProfiler.callCount.keySet()) {
            output.append(s.toLongString());
            output.append(" ");
            output.append(callCount.get(s));
            output.append(" ");
            output.append(timeCount.get(s));
            output.append(System.lineSeparator());
        }

        Path path = Paths.get("###STAT");
        Files.write(path, output.toString().getBytes(StandardCharsets.UTF_8));
    }
}
