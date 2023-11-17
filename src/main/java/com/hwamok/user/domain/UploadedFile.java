package com.hwamok.user.domain;

import com.hwamok.utils.PreConditions;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@Embeddable
@NoArgsConstructor
public class UploadedFile {
  private String originalFileName;
  private String savedFileName;

  public UploadedFile(String originalFileName, String savedFileName) {
    PreConditions.require(Strings.isNotBlank(originalFileName));
    PreConditions.require(Strings.isNotBlank(savedFileName));

    this.originalFileName = originalFileName;
    this.savedFileName = savedFileName;
  }
}
