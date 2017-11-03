package com.netcrcaker;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateCSV {

  private Set<String> requiredItems = new HashSet<>();
  private Set<String> rootRequiredItems = new HashSet<>();
  private String parent;
  private StringBuilder scv = new StringBuilder();
  private Map<String, Object> data = new LinkedHashMap<>();
  private final String idRegex = "(o|\\d)*-\\d*-\\d*";
  private List<String> parents = new ArrayList<>();
  private int count = 0;
  private String mainDescription;

  private void setNameAndColumns() {

    this.mainDescription = this.data.get(Constants.DESCRIPTION).toString();
    scv.append(this.mainDescription);
    for (int i = 0; i < 7; i++) {
      scv.append(";");
    }
    scv.append("\n").append("ID;PARENT;ATTR_NAME;TYPE;APPEARANCE;VALUE;DESCRIPTION;REQUIRED")
        .append("\n");
  }

  @SuppressWarnings("unchecked")
  private void setRequired(List<String> data) {
    this.requiredItems.addAll(data);
  }

  private boolean isRootRequired(String name) {
    for (String attrName : this.rootRequiredItems) {
      if (attrName.equals(name)) {
        return true;
      }
    }
    return false;
  }


  @SuppressWarnings("unchecked")
  public void generate() {
    parents.add(null);
    setNameAndColumns();
    if (this.data.get(Constants.PROPERTIES) != null) {
      Map<String, Object> map = (Map<String, Object>) this.data.get(Constants.PROPERTIES);
      rootRequiredItems = map.keySet();
    }
    fillLine(this.data);
    log.info(scv.toString().trim());
    log.info("\n" + data.toString());
    new OutputScvWriter().writeCsv(this.mainDescription, scv.toString().trim());
  }

  @SuppressWarnings("unchecked")
  private void fillLine(Map<String, Object> data) {

    String description = null;
    if (data.get(Constants.REQUIRED) != null) {
      setRequired((List<String>) data.get(Constants.REQUIRED));
    }
    if (data.get(Constants.PROPERTIES) == null) {

      for (Map.Entry<String, Object> insideOfPropertiesValue : data.entrySet()) {

        Map<String, Object> inner = (Map<String, Object>) insideOfPropertiesValue.getValue();
        if (inner.get(Constants.DESCRIPTION) != null) {
          description = parseDescription((String) inner.get(Constants.DESCRIPTION));
        }
        if (this.parent != null && !isRootRequired(insideOfPropertiesValue.getKey())) {
          scv.append(this.parent).append(";");
        } else {
          this.scv.append(";");
        }

        if (insideOfPropertiesValue.getKey() != null) {
          this.scv.append(insideOfPropertiesValue.getKey()).append(";");
        } else {
          scv.append(";");
        }
        if (inner.get(Constants.TYPE) != null) {
          scv.append(inner.get(Constants.TYPE)).append(";");
          if (inner.get(Constants.TYPE).equals(Constants.STRING_TYPE)) {
            if (inner.get(Constants.ENUM) != null && !inner.get(Constants.ENUM).toString().trim()
                .equals("[]")) {
              if (inner.get(Constants.ENUM).toString().contains(",") && !inner.get(Constants.ENUM)
                  .toString()
                  .contains("{")) {
                String prettyEnum = inner.get(Constants.ENUM).toString()
                    .replaceAll(" ", "")
                    .replaceAll("\\[", "^(")
                    .replaceAll(",", "|")
                    .replaceAll("]", ")\\$");

                scv.append(Constants.ENUM).append(";").append(prettyEnum).append(";");
              } else {
                scv.append(Constants.ENUM).append(";").append(inner.get(Constants.ENUM).toString()
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")).append(";");
              }
            } else if (inner.get(Constants.PATTERN) != null) {
              scv.append(Constants.PATTERN).append(";").append(inner.get(Constants.PATTERN))
                  .append(";");
            } else {
              scv.append(";");
              scv.append(";");
            }
          } else if (inner.get(Constants.TYPE).equals(Constants.INTEGER_TYPE)) {
            scv.append(";");
            if (inner.containsKey(Constants.MINIMUM)) {
              scv.append("\"").append("\"").append("\"").append(Constants.MINIMUM).append("\"")
                  .append("\"")
                  .append(":").append(inner.get(Constants.MINIMUM))
                  .append(",").append("\"").append("\"").append(Constants.MAXIMUM).append("\"")
                  .append("\"")
                  .append(":").append(inner.get(Constants.MAXIMUM))
                  .append(",").append("\"").append("\"").append(Constants.EXCLUSIVE_MINIMUM)
                  .append("\"")
                  .append("\"").append(":").append(inner.get(Constants.EXCLUSIVE_MINIMUM))
                  .append("\"");
            }
            scv.append(";");
          } else {
            scv.append(";").append(";");
          }
        } else {
          scv.append(";");
        }

        this.scv.append(description).append(";");

        if (isRequired(insideOfPropertiesValue.getKey())) {
          this.scv.append(Constants.REQUIRED_TRUE);
        } else {
          this.scv.append(Constants.REQUIRED_FALSE);
        }
        if (inner.get(Constants.PROPERTIES) != null) {
          parent = insideOfPropertiesValue.getKey();
          parents.add(parent);
          ++count;
          scv.append("\n");
          fillLine(inner);
        }
        if (scv.lastIndexOf("\n") != scv.length() - 1) {
          scv.append("\n");
        } else {
          this.parent = parents.get(parents.size() - count);
        }
      }
    } else {
      fillLine((Map<String, Object>) data.get(Constants.PROPERTIES));
    }
  }

  private String parseDescription(String fullDescription) {
    Pattern pattern = Pattern.compile(this.idRegex);
    Matcher matcher = pattern.matcher(fullDescription);
    if (matcher.find()) {
      String id = matcher.group(0);
      scv.append(id).append(";");
      return fullDescription.replaceAll(id, "").trim();
    } else {
      scv.append(";");
    }
    scv.append(";");
    return fullDescription;
  }

  private boolean isRequired(String key) {
    for (String req : this.requiredItems) {
      if (req.equals(key)) {
        return true;
      }
    }
    return false;
  }

  public void setData(Multimap<String, Object> data) {
    System.out.println();
//    this.data = data;
  }
}