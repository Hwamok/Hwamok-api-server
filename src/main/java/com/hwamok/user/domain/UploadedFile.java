package com.hwamok.user.domain;

import com.hwamok.core.Preconditions;
import jakarta.persistence.Column;
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

    Preconditions.require(Strings.isNotBlank(originalFileName));
    Preconditions.require(Strings.isNotBlank(savedFileName));

    this.originalFileName = originalFileName;
    this.savedFileName = savedFileName;
  }

  public void updateUploadedFile(String originalFileName, String savedFileName) {

    Preconditions.require(Strings.isNotBlank(originalFileName));
    Preconditions.require(Strings.isNotBlank(savedFileName));

    this.originalFileName = originalFileName;
    this.savedFileName = savedFileName;
  }

}
