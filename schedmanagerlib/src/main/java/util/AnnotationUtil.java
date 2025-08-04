package util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {
    /**
     * Retrieves a list of all annotation names for the given object.
     *
     * @param obj the object whose annotations are to be retrieved
     *
     * @return a list of annotation names
     */
    public static List<String> getAnnotationNames(Object obj) {
        List<String> annotationNames = new ArrayList<>();
        Class<?> objClass = obj.getClass();

        for (Annotation annotation : objClass.getAnnotations()) {
            annotationNames.add(annotation.annotationType().getName());
        }

        return annotationNames;
    }
}
