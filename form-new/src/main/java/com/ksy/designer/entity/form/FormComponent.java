package com.ksy.designer.entity.form;

import java.util.Map;

import com.ksy.designer.entity.IComponent;

public class FormComponent implements IComponent {
    private String id;
    private String name;
    private Map<?, ?> attribute;

    public FormComponent(String id, String name, Map<?, ?> attribute) {
        this.id = id;
        this.name = name;
        this.attribute = attribute;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<?, ?> getAttribute() {
        return this.attribute;
    }

}
