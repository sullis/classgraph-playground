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
      try (ScanResult result = graph.scan()) {
        assertThat(result.getAllClasses()).isNotEmpty();
      }
  }
}
