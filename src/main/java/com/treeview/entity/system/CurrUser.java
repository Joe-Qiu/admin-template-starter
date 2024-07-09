package com.treeview.entity.system;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CurrUser implements Serializable {
    private static final long serialVersionUID = -8199544345277102183L;

    private Long id;

    private String userName;

    private String nickName;

    private String avatar;
}