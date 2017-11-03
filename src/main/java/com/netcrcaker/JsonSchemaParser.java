package com.netcrcaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonSchemaParser {

  public void parse(String json) {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(json))) {
      for (String line = bufferedReader.readLine(); line != null;
          line = bufferedReader.readLine()) {
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      log.error("Error while reading file");
    }
    final String json1 = stringBuilder.toString();

    ObjectMapper om = new ObjectMapper();
    om.registerModule(new GuavaModule());

    try {
      Multimap<String, Object> navs = om.readValue(
          om.treeAsTokens(om.readTree(json1)),
          om.getTypeFactory().constructMapLikeType(
              Multimap.class, String.class, Object.class));



//      Multimap<String, Object>  data = mapper.readValue(json1, Multimap.class);
//      System.out.println(data);
      System.out.println(navs);
//      GenerateCSV generateCSV = new GenerateCSV();
//      generateCSV.setData(data);
//      generateCSV.generate();

    } catch (IOException e) {
      log.error("Error while generating csv");
      e.printStackTrace();
    }
  }
}
