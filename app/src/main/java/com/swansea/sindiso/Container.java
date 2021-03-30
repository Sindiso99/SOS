package com.swansea.sindiso;

public class Container {
    private String label;
    private String description;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer volume;

    public Container(String label, Integer l, Integer h, Integer w, String description){
        this.label = label;
        this.length = l;
        this.height = h;
        this.width = w;
        this.description = description;
        setVolume();
    }

    @Override
    public String toString() {
        return "Container{" +
                "label='" + label + '\'' +
                ", volume=" + volume +
                '}';
    }

    private boolean validUnit(Integer unit){
        if(unit >= 0){
            return true;
        } else {
            return false;
        }
    }

    public void setVolume(){
        volume = length * width * height;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWidth(Integer w){
        if(validUnit(w)){
            width = w;
        }
    }

    public void setHeight(Integer h){
        if (validUnit(h)){
            height = h;
        }
    }

    public void setLength(Integer l){
        if (validUnit(l)){
            length = l;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
