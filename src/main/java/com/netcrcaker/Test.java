package com.netcrcaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

  public static void main(String[] args) throws IOException {

    String before = new String(
        Files.readAllBytes(Paths.get(
            "C:\\Users\\anku0717\\Desktop\\Projects\\json_schema_to_csv\\src\\main\\resources\\before.csv")));

    String after = new String(
        Files.readAllBytes(Paths.get(
            "C:\\Users\\anku0717\\Desktop\\Projects\\json_schema_to_csv\\src\\main\\resources\\after.csv")));

    List<String> beforeList= Arrays.asList(before.split("\n"));
    List<String> afterList= Arrays.asList(after.split("\n"));
    Collections.sort(beforeList);
    Collections.sort(afterList);

    System.out.println("Before");
    for(String x:beforeList){
      System.out.println(x);
    }
    System.out.println();
    System.out.println();

    System.out.println("After");
    for(String x:afterList){
      System.out.println(x);
    }
    System.out.println();
    System.out.println();

  }

}
