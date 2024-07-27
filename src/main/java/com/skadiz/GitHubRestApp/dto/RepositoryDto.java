package com.skadiz.GitHubRestApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RepositoryDto {
    private String name;
    private OwnerDto owner;
    private List<BranchDto> branches = new ArrayList<>();
    private boolean fork;
}
