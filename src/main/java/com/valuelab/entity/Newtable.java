package com.valuelab.entity;

import lombok.Data;

import java.io.Serializable;

import com.valuelab.form.NewtableForm;

@Data
public class Newtable implements Serializable {

    public static Newtable of(NewtableForm newtableForm) {

        Newtable newtable = new Newtable();

        newtable.setId(newtableForm.getId());
        newtable.setName(newtableForm.getName());

        return newtable;
    }

    private Long id;
    private String name;

}
