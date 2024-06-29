package com.kenter7317;

import com.google.auto.service.AutoService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
@AutoService(Processor.class)
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
            ProcessingEnvironment pe = processingEnv;
            InputStream ymlStream;
            try {
                ymlStream = pe.getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "", registry.key() + ".yml").openInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Yaml yaml = new Yaml();
            Map load = yaml.load(ymlStream);
            pe.getMessager().printMessage(Diagnostic.Kind.NOTE, load.get(registry.value()).toString());
            try {
                Field f = field.getClass().getDeclaredField(field.getSimpleName().toString());
                f.setAccessible(true);
                f.set(field.getEnclosingElement().getClass(), load.get(registry.value()));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
