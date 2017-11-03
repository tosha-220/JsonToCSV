package com.netcrcaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class OutputScvWriter {

  private static final String SCV = "scv/";

  void writeCsv(String schemaName, String fullJ) {

    File schemaFolder = new File(SCV);
    if (!schemaFolder.exists()) {
      schemaFolder.mkdir();
    }
    try (BufferedWriter bufferedWriter = new BufferedWriter(
        new FileWriter(schemaFolder + "/" + schemaName + ".csv"))) {
      log.info("Starting writing file " + schemaName + ".csv");
      bufferedWriter.write(fullJ);
      bufferedWriter.flush();

    } catch (FileNotFoundException e) {
      log.error("Path not found", e);
    } catch (IOException e) {
      log.error("Error while writing file", e);
    }
    log.info("Completed");
  }
}
