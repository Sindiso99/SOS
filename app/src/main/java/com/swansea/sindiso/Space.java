package com.swansea.sindiso;

public class Space {
    private Integer length;
    private Integer height;
    private Integer width;
    private String description;
    private Integer ownerId;
    private Integer volume;

    public Space(Integer ownerId, Integer length, Integer height, Integer width, String description) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.description = description;
        this.ownerId = ownerId;
        setVolume();
    }

    private void setVolume(){
        volume = length * height * width;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getDescription() {
        return description;
    }

    public Integer getOwnerId() {
        return ownerId;
    }
}
