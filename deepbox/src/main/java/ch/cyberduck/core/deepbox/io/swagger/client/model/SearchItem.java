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
import ch.cyberduck.core.deepbox.io.swagger.client.model.NodePolicy;
import ch.cyberduck.core.deepbox.io.swagger.client.model.Path;
import ch.cyberduck.core.deepbox.io.swagger.client.model.Tag;
import ch.cyberduck.core.deepbox.io.swagger.client.model.TimeUserContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * SearchItem
 */



public class SearchItem {
  @JsonProperty("nodeId")
  private String nodeId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("mimeType")
  private String mimeType = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FOLDER("folder"),
    FILE("file");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String input) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("modified")
  private TimeUserContext modified = null;

  @JsonProperty("policy")
  private NodePolicy policy = null;

  @JsonProperty("size")
  private Long size = null;

  @JsonProperty("analysisStatus")
  private String analysisStatus = null;

  @JsonProperty("tags")
  private List<Tag> tags = null;

  @JsonProperty("parentPath")
  private Path parentPath = null;

  public SearchItem nodeId(String nodeId) {
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

  public SearchItem name(String name) {
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

  public SearchItem mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

   /**
   * Get mimeType
   * @return mimeType
  **/
  @Schema(description = "")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public SearchItem type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @Schema(description = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SearchItem modified(TimeUserContext modified) {
    this.modified = modified;
    return this;
  }

   /**
   * Get modified
   * @return modified
  **/
  @Schema(description = "")
  public TimeUserContext getModified() {
    return modified;
  }

  public void setModified(TimeUserContext modified) {
    this.modified = modified;
  }

  public SearchItem policy(NodePolicy policy) {
    this.policy = policy;
    return this;
  }

   /**
   * Get policy
   * @return policy
  **/
  @Schema(description = "")
  public NodePolicy getPolicy() {
    return policy;
  }

  public void setPolicy(NodePolicy policy) {
    this.policy = policy;
  }

  public SearchItem size(Long size) {
    this.size = size;
    return this;
  }

   /**
   * Get size
   * @return size
  **/
  @Schema(description = "")
  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public SearchItem analysisStatus(String analysisStatus) {
    this.analysisStatus = analysisStatus;
    return this;
  }

   /**
   * Get analysisStatus
   * @return analysisStatus
  **/
  @Schema(description = "")
  public String getAnalysisStatus() {
    return analysisStatus;
  }

  public void setAnalysisStatus(String analysisStatus) {
    this.analysisStatus = analysisStatus;
  }

  public SearchItem tags(List<Tag> tags) {
    this.tags = tags;
    return this;
  }

  public SearchItem addTagsItem(Tag tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Get tags
   * @return tags
  **/
  @Schema(description = "")
  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public SearchItem parentPath(Path parentPath) {
    this.parentPath = parentPath;
    return this;
  }

   /**
   * Get parentPath
   * @return parentPath
  **/
  @Schema(description = "")
  public Path getParentPath() {
    return parentPath;
  }

  public void setParentPath(Path parentPath) {
    this.parentPath = parentPath;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchItem searchItem = (SearchItem) o;
    return Objects.equals(this.nodeId, searchItem.nodeId) &&
        Objects.equals(this.name, searchItem.name) &&
        Objects.equals(this.mimeType, searchItem.mimeType) &&
        Objects.equals(this.type, searchItem.type) &&
        Objects.equals(this.modified, searchItem.modified) &&
        Objects.equals(this.policy, searchItem.policy) &&
        Objects.equals(this.size, searchItem.size) &&
        Objects.equals(this.analysisStatus, searchItem.analysisStatus) &&
        Objects.equals(this.tags, searchItem.tags) &&
        Objects.equals(this.parentPath, searchItem.parentPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, name, mimeType, type, modified, policy, size, analysisStatus, tags, parentPath);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchItem {\n");
    
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    analysisStatus: ").append(toIndentedString(analysisStatus)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    parentPath: ").append(toIndentedString(parentPath)).append("\n");
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