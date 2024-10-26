package foobar;

import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import io.github.classgraph.ClassGraph;

public class ClassgraphTest {
  @Test
  public void findClasses() {
      ClassGraph graph = new ClassGraph();
      graph.enableClassInfo();
      graph.disableJarScanning();
      graph.disableNestedJarScanning();
      try (ScanResult result = graph.scan()) {
        assertThat(result.getAllClasses()).isNotEmpty();
        result.getAllClasses().forEach(clazz -> System.out.println("Found: " + clazz + " in file: " + clazz.getSourceFile()));
      }
  }
  @Test
  public void findResourceFile() {
    ClassGraph graph = new ClassGraph();
    graph.disableJarScanning();
    graph.disableNestedJarScanning();
    try (ScanResult result = graph.scan()) {
      assertThat(result.getAllResources()).isNotEmpty();
      var dataTextList = result.getAllResources().get("data.txt");
      assertThat(dataTextList).hasSize(1);
      var resource = dataTextList.get(0);
      System.out.println("resource: " + resource);
      System.out.println("path: " + resource.getPath());
      System.out.println("classpath uri: " + resource.getClasspathElementURI());
    }
  }
}
