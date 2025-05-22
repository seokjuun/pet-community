package com.potato.petpotatocommunity.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@Table(name="group_code")
public class GroupCode implements Persistable<String> {

    @Id
    @Column(name="group_code")
    private String groupCode;

    @Column(name="group_code_name")
    private String groupCodeName;

    @Column(name="group_code_desc")
    private String groupCodeDesc;

    @Column(name="is_active") // 1 활성화, 0 비활성화
    private int isActive;

    @Transient
    private boolean isNew = false;

    public String getId(){
        return groupCode;
    }

}
