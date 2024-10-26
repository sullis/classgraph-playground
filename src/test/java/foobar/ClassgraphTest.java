package foobar;

import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import io.github.classgraph.ClassGraph;

public class ClassgraphTest {
  @Test
  public void foo() {
      ClassGraph graph = new ClassGraph();
      graph.enableClassInfo();
      graph.disableJarScanning();
      graph.disableNestedJarScanning();
      try (ScanResult result = graph.scan()) {
        assertThat(result.getAllClasses()).isNotEmpty();
        result.getAllClasses().forEach(clazz -> System.out.println("Found: " + clazz + " in file: " + clazz.getSourceFile()));
      }
  }
}
