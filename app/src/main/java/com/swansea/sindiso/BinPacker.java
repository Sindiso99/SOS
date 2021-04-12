package com.swansea.sindiso;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BinPacker {

    private List<Container> unplacedContainers;
    private List<Container> placedContainers;
    private List<Container> allContainers;
    private List<Space> houses; //bins
    private DataBaseHandler dataBaseHandler;
    private User student;
    private Context context;
    private Space chosenHouse;
    private Integer availableCells;
    Integer[][] spaceLayout;
    private Integer boxCellWidth;
    private Integer boxCellLength;
    private Integer boxSize;
    private FileHandler fileHandler;

    public BinPacker(Context context, User student, List<Space> houses){
        this.student = student;
        this.context = context;
        this.houses = houses;
        dataBaseHandler = new DataBaseHandler(this.context);
        this.student.setContainers(dataBaseHandler.getContainers(this.student.getId()));
        allContainers = this.student.getContainers();
        unplacedContainers = allContainers;
        placedContainers = new ArrayList<>();
        fileHandler = new FileHandler(context);
    }

    public boolean pack(){
        if(twoDimensionalPacker()){
            if(fileHandler.writeFloorPlan(chosenHouse) && dataBaseHandler.addMatch(student.getId(), chosenHouse.getOwnerId())) {
                return true;
            }
        }
        return false;
    }

    public Space getChosenHouse(){
        return chosenHouse;
    }
    private boolean twoDimensionalPacker() {
        for (Space curSpace : houses) {
            Space tempSpace = curSpace;
            if (fileHandler.checkExistence(tempSpace.getOwnerId())){
                tempSpace.importGrid(fileHandler.readFloorPlan(tempSpace.getOwnerId(), tempSpace.getGridLength(), tempSpace.getGridWidth()));
            } else {
                tempSpace.initialiseGrid();
            }
            if (checkIfFits(tempSpace)){
                for (Container curContainer : allContainers) {
                    int lengthToFit = curContainer.lengthToCell();
                    int widthToFit = curContainer.widthToCell();
                    iterator: for (int x = 0; x < tempSpace.getGridLength(); x++){
                        for (int y = 0; y < tempSpace.getGridWidth(); y++){
                            if(checkPossibleLocation(tempSpace, x, y, lengthToFit, widthToFit)){
                               tempSpace.fitContainer(curContainer.getId(), lengthToFit, widthToFit, x, y);
                               placedContainers.add(curContainer);
                                break iterator;
                            } else if(checkPossibleLocation(tempSpace, x, y, widthToFit, lengthToFit)){
                                tempSpace.fitContainer(curContainer.getId(), widthToFit, lengthToFit, x, y);
                                placedContainers.add(curContainer);
                                break iterator;
                            }
                        }
                    }
                }
                unplacedContainers.removeAll(placedContainers);
                if (unplacedContainers.size() == 0) {
                    chosenHouse = tempSpace;
                    return true;
                }
            }

        }
        return false;
    }

    private boolean checkPossibleLocation(Space space, int x, int y, int lengthToFit, int widthToFit) {
        int remainingWidth = widthToFit;
        int remainingLength = lengthToFit;
            for (int tempX = x; tempX < space.getGridLength() && remainingLength > 0; tempX++){
                for (int tempY = y; tempY < space.getGridWidth() && remainingWidth > 0; tempY++){
                    if (space.getFloorCell(tempX, tempY) == -1){
                        remainingWidth --;
                    } else {
                        return false;
                    }
                }
                remainingLength --;
            }
        if(remainingLength > 0 || remainingWidth > 0){
            return false;
        } else { return true; }
    }



    private boolean checkIfFits (Space space) {
        Integer totalCells = 0;
        for (Container curContainer : unplacedContainers) {
            boxCellLength = curContainer.lengthToCell();
            boxCellWidth = curContainer.widthToCell();
            boxSize = boxCellLength * boxCellWidth;
            totalCells += boxSize;
        }
        if (totalCells > space.getAvailableCells()) {
            return false;
        } else {
            return true;
        }
    }


}


