package profiler;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
//        if (args == null || args.length != 3 || Arrays.stream(args).anyMatch(Objects::isNull)) {
//            System.out.println("Expected arguments: <package name> <path to package> <path to save statistics>");
//            return;
//        }

//        String packageName = args[0];
//        String pathToPackage = args[1];
//        String pathToStatistics = args[2];

        String packageName = "bank";
        String pathToPackage = "/home/tihonovcore/IdeaProjects/SoftwareDesign/aop/src/main/java/";
        String pathToStatistics = "/home/tihonovcore/IdeaProjects/SoftwareDesign/aop/target/statistics.txt";

        String pathToProfiler = System.getProperty("user.dir") + "/aop/src/main/resources/AspectProfiler.java";

        new Main().run(pathToPackage, pathToProfiler, pathToStatistics, packageName);
    }

    private void run(
           String pathToPackage,
           String pathToProfiler,
           String pathToStatistics,
           String packageName
    ) throws IOException {
        copyProfiler(pathToPackage, pathToProfiler, pathToStatistics, packageName);
        compile();
        removeProfiler(pathToPackage);
    }

    private void copyProfiler(
            String pathToPackage,
            String pathToProfiler,
            String pathToStatistics,
            String packageName
    ) throws IOException {
        byte[] profilerBytes = Files.readAllBytes(Paths.get(pathToProfiler));
        String profilerText = new String(profilerBytes);

        profilerText = setPackage(profilerText, packageName);
        profilerText = setStatisticsPath(profilerText, pathToStatistics);

        Path pathToCopy = Paths.get(pathToPackage + packageName.replace('.', '/') + "/AspectProfiler.java");
        Files.write(pathToCopy, profilerText.getBytes());
    }

    private String setPackage(String text, String packageName) {
        return text.replace("###PACKAGE", packageName);
    }

    private String setStatisticsPath(String text, String pathToStatistics) {
        return text.replace("###STAT", pathToStatistics);
    }

    private void compile() throws IOException {
        String path = "/home/tihonovcore/IdeaProjects/SoftwareDesign/aop/src/main/java/bank";
        String source = "/home/tihonovcore/.m2/repository/org/aspectj/aspectjrt/1.9.6/aspectjrt-1.9.6.jar";
        String dest = "/home/tihonovcore/IdeaProjects/SoftwareDesign/aop/target";
        org.aspectj.tools.ajc.Main.main(new String[]{"-1.8", "-cp", source, "-d", dest, "-sourceroots", path});
    }

    private void removeProfiler(String pathToPackage) throws IOException {
        Files.delete(Paths.get(pathToPackage + "/AspectProfiler.java"));
    }
}
