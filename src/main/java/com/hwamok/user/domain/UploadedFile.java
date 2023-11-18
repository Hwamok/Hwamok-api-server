package com.hwamok.user.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.hwamok.utils.PreConditions.*;

@Getter
@Embeddable
@NoArgsConstructor
public class UploadedFile {
  private String originalFileName;

  private String savedFileName;

  public UploadedFile(String originalFileName, String savedFileName) {
    require(Strings.isNotBlank(originalFileName));
    require(Strings.isNotBlank(savedFileName));

    this.originalFileName = originalFileName;
    this.savedFileName = savedFileName;
  }
}
