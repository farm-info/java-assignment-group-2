package main.system;

import java.util.UUID;

public abstract class BaseItem {
    protected String Id;

    public BaseItem() {
        this.Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    protected void setId(String id) {
        this.Id = id;
    }
}
