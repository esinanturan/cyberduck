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
 * Watch
 */



public class Watch {
  @JsonProperty("watchId")
  private String watchId = null;

  @JsonProperty("deepBoxNodeId")
  private String deepBoxNodeId = null;

  @JsonProperty("boxNodeId")
  private String boxNodeId = null;

  @JsonProperty("boxName")
  private String boxName = null;

  @JsonProperty("nodeId")
  private String nodeId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("recursive")
  private Boolean recursive = null;

  public Watch watchId(String watchId) {
    this.watchId = watchId;
    return this;
  }

   /**
   * Get watchId
   * @return watchId
  **/
  @Schema(description = "")
  public String getWatchId() {
    return watchId;
  }

  public void setWatchId(String watchId) {
    this.watchId = watchId;
  }

  public Watch deepBoxNodeId(String deepBoxNodeId) {
    this.deepBoxNodeId = deepBoxNodeId;
    return this;
  }

   /**
   * Get deepBoxNodeId
   * @return deepBoxNodeId
  **/
  @Schema(description = "")
  public String getDeepBoxNodeId() {
    return deepBoxNodeId;
  }

  public void setDeepBoxNodeId(String deepBoxNodeId) {
    this.deepBoxNodeId = deepBoxNodeId;
  }

  public Watch boxNodeId(String boxNodeId) {
    this.boxNodeId = boxNodeId;
    return this;
  }

   /**
   * Get boxNodeId
   * @return boxNodeId
  **/
  @Schema(description = "")
  public String getBoxNodeId() {
    return boxNodeId;
  }

  public void setBoxNodeId(String boxNodeId) {
    this.boxNodeId = boxNodeId;
  }

  public Watch boxName(String boxName) {
    this.boxName = boxName;
    return this;
  }

   /**
   * Get boxName
   * @return boxName
  **/
  @Schema(description = "")
  public String getBoxName() {
    return boxName;
  }

  public void setBoxName(String boxName) {
    this.boxName = boxName;
  }

  public Watch nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

   /**
   * Get nodeId
   * @return nodeId
  **/
  @Schema(description = "")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public Watch name(String name) {
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

  public Watch recursive(Boolean recursive) {
    this.recursive = recursive;
    return this;
  }

   /**
   * Get recursive
   * @return recursive
  **/
  @Schema(description = "")
  public Boolean isRecursive() {
    return recursive;
  }

  public void setRecursive(Boolean recursive) {
    this.recursive = recursive;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Watch watch = (Watch) o;
    return Objects.equals(this.watchId, watch.watchId) &&
        Objects.equals(this.deepBoxNodeId, watch.deepBoxNodeId) &&
        Objects.equals(this.boxNodeId, watch.boxNodeId) &&
        Objects.equals(this.boxName, watch.boxName) &&
        Objects.equals(this.nodeId, watch.nodeId) &&
        Objects.equals(this.name, watch.name) &&
        Objects.equals(this.recursive, watch.recursive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(watchId, deepBoxNodeId, boxNodeId, boxName, nodeId, name, recursive);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Watch {\n");
    
    sb.append("    watchId: ").append(toIndentedString(watchId)).append("\n");
    sb.append("    deepBoxNodeId: ").append(toIndentedString(deepBoxNodeId)).append("\n");
    sb.append("    boxNodeId: ").append(toIndentedString(boxNodeId)).append("\n");
    sb.append("    boxName: ").append(toIndentedString(boxName)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    recursive: ").append(toIndentedString(recursive)).append("\n");
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