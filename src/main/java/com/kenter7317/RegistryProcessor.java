package com.kenter7317;

import com.google.auto.service.AutoService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map;
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
            ProcessingEnvironment pe = processingEnv;
            Class<?> clazz;
            try {
                clazz = field.getClass().getClassLoader().loadClass(field.getEnclosingElement().getSimpleName().toString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            FileObject objectFile;
            Filer filer = pe.getFiler();
            Yaml yaml = new Yaml();
            Map<String, String> load;
            try {
                objectFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", registry.key());
                load = yaml.load(objectFile.getCharContent(true).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            pe.getMessager().printMessage(Diagnostic.Kind.NOTE, load.get(registry.value()).toString());
            try {
                Field f = clazz.getDeclaredField(field.getSimpleName().toString());
                f.setAccessible(true);
                f.set(field.asType(), load.get(registry.value()));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
