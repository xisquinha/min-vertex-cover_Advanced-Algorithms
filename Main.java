import test.TestRunner;

public class Main {
  public static void main(String[] args) {

    // Criar o executor de testes
    TestRunner runner = new TestRunner(2);

    // Correr todos os cenários gerados
    runner.runAllTests();

  }
}
