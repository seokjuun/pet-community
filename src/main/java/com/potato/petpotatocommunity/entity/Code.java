package com.potato.petpotatocommunity.entity;

import com.potato.petpotatocommunity.entity.key.CodeKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Code {

    @EmbeddedId
    private CodeKey codeKey;

    @Column(name="code_name")
    private String codeName;

    @Column(name="code_name_brief")
    private String codeNameBrief; // 약어

    @Column(name="order_no")
    private int orderNo;

    @Column(name="is_active") // 1 활성화, 0 비활성화
    private int isActive;
}
