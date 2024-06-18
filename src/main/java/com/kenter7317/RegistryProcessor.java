package com.kenter7317;

import com.google.auto.service.AutoService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.File;
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
        for (Element element : elements) {
            Registry registry = element.getAnnotation(Registry.class);
            Field field = Field.class.cast(element);
            Yaml yaml = new Yaml();
            InputStream inputStream = field.getDeclaringClass().getClassLoader().getResourceAsStream(registry.key() + ".yml");
            System.out.println(field);
            if (inputStream == null){
                File file = new File(field.getDeclaringClass().getClassLoader().getResource(registry.key() + ".yaml").getFile());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Map<String, String> load = yaml.load(inputStream);
            System.out.println(load);
            try {
                field.set(field.getType(),load.get(registry.value()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
