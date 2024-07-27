package com.skadiz.GitHubRestApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BranchDto {
    private String name;
    private CommitDto commit;
}
