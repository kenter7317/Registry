package com.kenter7317;

import com.google.auto.service.AutoService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class RegistryProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Registry.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Registry.class);
        Set<VariableElement> fields = ElementFilter.fieldsIn(elements);
        for (VariableElement field : fields) {
            Registry registry = field.getAnnotation(Registry.class);
            if (field.getEnclosingElement().getKind() == ElementKind.CLASS) {
                try {
                    this.getClass().getClassLoader().loadClass(field.getEnclosingElement().getClass().getName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            Yaml yaml = new Yaml();
            try {
                List<? extends Element> enclosedElements = field.getEnclosedElements();
                ProcessingEnvironment pe = processingEnv;
//                InputStream ymlStream;
//                FileObject fileObject = pe.getFiler()
//                        .getResource(StandardLocation.CLASS_OUTPUT, "", registry.key() + ".yml");
//                InputStream jsonStream = fileObject.openInputStream();
//                ymlStream = jsonStream;
//                Map load = yaml.load(ymlStream);
//                pe.getMessager().printMessage(Diagnostic.Kind.NOTE, load.get(registry.value()).toString());
                try {
                    Field f = field.getClass().getClassLoader().loadClass(field.getEnclosingElement().getSimpleName().toString()).getDeclaredField(field.getSimpleName().toString());
                    f.setAccessible(true);
                    f.set(null, "test");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
//                    jsonStream.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        return true;
    }
}
