package com.netcrcaker;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  public static void main(String[] args) {
    final String path = args[0];
    File file = new File(path);
    try {
      if (!file.exists()) {
        throw new FileNotFoundException();
      }
      new JsonSchemaParser().parse(path);
    } catch (FileNotFoundException e) {
      log.error("File not found {}", args[0]);
    }
  }
}
