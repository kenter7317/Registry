import com.kenter7317.Registry;
import com.kenter7317.RegistryProcessor;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

public class Runner {
    @Registry(value = "abc")
    private static String test;

    public static void main(String[] args) throws NoSuchFieldException {
        Runner.class.getDeclaredField("test").getAnnotation(Registry.class).value(); // Should print "abc"
        System.out.println(test);
    }

    @TempDir
    Path tempDir;


    @Test
    public void test() throws IOException {
        var compiler = ToolProvider.getSystemJavaCompiler();

        var diagnostics = new DiagnosticCollector<JavaFileObject>();

        var fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tempDir.toFile()));
        fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(tempDir.toFile()));

        var file = new File("src/test/java/Runner.java");
        var compilationUnits = fileManager.getJavaFileObjects(file);

        var task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        task.setProcessors(Collections.singletonList(new RegistryProcessor()));

        // When
        boolean success = task.call();

        // Then
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.err.println(diagnostic);
        }

        System.out.println(test);
    }

}

