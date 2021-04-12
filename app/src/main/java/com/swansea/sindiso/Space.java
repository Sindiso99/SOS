package com.swansea.sindiso;

import java.util.ArrayList;
import java.util.List;

public class Space {
    private Integer length;
    private Integer height;
    private Integer width;
    private String description;
    private Integer ownerId;
    private Integer volume;
    private List<Container> containers;
    private Integer[][] floorLayout;
    private Integer availableCells;
    private Integer gridWidth;
    private Integer gridLength;

    public Space(Integer ownerId, Integer length, Integer height, Integer width, String description) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.description = description;
        this.ownerId = ownerId;
        setVolume();
        containers = new ArrayList<>();
        gridLength = length * 2;
        gridWidth = width * 2;
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

    public void initialiseGrid() {
        floorLayout = new Integer[gridLength][gridWidth];
        setGridToEmpty();
        availableCells = gridLength * gridWidth;
    }

    private void setGridToEmpty(){
        for (int x = 0; x < gridLength; x++){
            for(int y = 0; y < gridWidth; y++){
                floorLayout[x][y] = -1;
            }
        }
    }

    public void importGrid(Integer[][] grid){
        floorLayout = grid;
        availableCells = 0;
        for (int x = 0; x < gridLength; x++){
            for (int y = 0; y < gridWidth; y++){
                if (floorLayout[x][y] == -1) {
                    availableCells ++;
                }
            }
        }

    }

    public Integer getGridWidth() {
        return gridWidth;
    }

    public Integer getGridLength() {
        return gridLength;
    }

    public Integer[][] getFloorLayout() {
        return floorLayout;
    }

    public Integer getAvailableCells() {
        return availableCells;
    }

    public void addContainer(Container container){
        containers.add(container);
    }

    public Integer getFloorCell(int x , int y){
        return floorLayout[x][y];
    }

    public List<Container> getAllContainers() {
        return containers;
    }

    public void fitContainer(Integer id, int lengthToFit, int widthToFit, int startX, int startY){
        for (int x = startX; x - startX < lengthToFit; x++){
            for (int y = startY; y - startY < widthToFit; y++){
                floorLayout[x][y] = id;
                availableCells--;
            }
        }
    }
}
