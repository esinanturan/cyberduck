/*
 * DeepBox
 * DeepBox API Documentation
 *
 * OpenAPI spec version: 1.0
 * Contact: info@deepcloud.swiss
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package ch.cyberduck.core.deepbox.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * BoxUpdate
 */



public class BoxUpdate {
  @JsonProperty("name")
  private String name = null;

  /**
   * Box Variant. null is simple box.
   */
  public enum BoxVariantEnum {
    ADVANCED("advanced"),
    ADVANCED_PER_USE("advanced-per-use");

    private String value;

    BoxVariantEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static BoxVariantEnum fromValue(String input) {
      for (BoxVariantEnum b : BoxVariantEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("boxVariant")
  private BoxVariantEnum boxVariant = null;

  public BoxUpdate name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BoxUpdate boxVariant(BoxVariantEnum boxVariant) {
    this.boxVariant = boxVariant;
    return this;
  }

   /**
   * Box Variant. null is simple box.
   * @return boxVariant
  **/
  @Schema(description = "Box Variant. null is simple box.")
  public BoxVariantEnum getBoxVariant() {
    return boxVariant;
  }

  public void setBoxVariant(BoxVariantEnum boxVariant) {
    this.boxVariant = boxVariant;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoxUpdate boxUpdate = (BoxUpdate) o;
    return Objects.equals(this.name, boxUpdate.name) &&
        Objects.equals(this.boxVariant, boxUpdate.boxVariant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, boxVariant);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoxUpdate {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    boxVariant: ").append(toIndentedString(boxVariant)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}