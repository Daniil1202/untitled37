package org.example;

import annotations.*;

import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        List<String> result = TestRun.run(TestRunner.class);
        System.out.println("\n\r - Результат тестов");
        result.stream().forEach(System.out::println);
    }
    @BeforeAll
    public void BeforeAll(){
        System.out.println("Команды перед всем текстом");
    }
    @BeforeEach
    public void BeforeEach(){
        System.out.println("Команды перед каждым тесктом");
    }
    public void AfterEach(){
        System.out.println("Команды после каждого текста");
    }
    public void AfterAll(){
        System.out.println("Команды после всего текста");
    }
    @Test
    public void test2(){
        System.out.println("Тест №3");
    }
    @Test
    public void test1(){
        System.out.println("Test№1");
    }
    @Test
    public void test0(){
        System.out.println("Test№0");
    }


}
