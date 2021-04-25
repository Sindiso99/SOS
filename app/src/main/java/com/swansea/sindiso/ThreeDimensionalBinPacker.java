package com.swansea.sindiso;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ThreeDimensionalBinPacker {
    private List<Container> unplacedContainers;
    private List<Container> placedContainers;
    private List<Container> allContainers;
    private List<Space> houses; //bins
    private DataBaseHandler dataBaseHandler;
    private User student;
    private Context context;
    private Space chosenHouse;
    private Integer boxCellWidth;
    private Integer boxCellLength;
    private Integer boxCellHeight;
    private Integer boxSize;
    private FileHandler fileHandler;

    public ThreeDimensionalBinPacker(Context context, User student, List<Space> houses){
        this.student = student;
        this.context = context;
        this.houses = houses;
        dataBaseHandler = new DataBaseHandler(this.context);
        this.student.setContainers(dataBaseHandler.getAllContainers(this.student.getId()));
        allContainers = this.student.getContainers();
        unplacedContainers = allContainers;
        placedContainers = new ArrayList<>();
        fileHandler = new FileHandler(context);
    }

    public boolean pack(){
        if(packer()){
            if(fileHandler.writeFloorSpace(chosenHouse) && dataBaseHandler.addMatch(student.getId(), chosenHouse.getOwnerId())) {
                return true;
            }
        }
        return false;
    }

    private boolean packer(){
        for (Space curSpace : houses) {
            Space tempSpace = curSpace;
            if (fileHandler.checkExistence(tempSpace.getOwnerId())){
                tempSpace.importSpace(fileHandler.readSpacePlan(tempSpace.getOwnerId(), tempSpace.getGridLength(), tempSpace.getGridWidth(), tempSpace.getGridHeight()));
            } else {
                tempSpace.initialiseSpace();
            }
            if (checkIfFits(tempSpace)){
                //initialise placed and unplaced containers
                placedContainers = new ArrayList<>();
                unplacedContainers = allContainers;
                for (Container curContainer : allContainers) {
                    int lengthToFit = curContainer.lengthToCell();
                    int widthToFit = curContainer.widthToCell();
                    int heightToFit = curContainer.heightToCell();

                    iterator: for (int x = 0; x < tempSpace.getGridLength(); x++){
                        for (int y = 0; y < tempSpace.getGridWidth(); y++){
                            for (int z = 0; z < tempSpace.getGridHeight(); z++){
                                if(checkPossibleLocation(tempSpace, x, y, z, lengthToFit, widthToFit, heightToFit)){
                                    tempSpace.fitContainer(curContainer.getId(), lengthToFit, widthToFit, heightToFit, x, y, z);
                                    placedContainers.add(curContainer);
                                    Toast.makeText(context, curContainer.getLabel() + " fits", Toast.LENGTH_SHORT).show();
                                    break iterator;
                                } else if(checkPossibleLocation(tempSpace, x, y, z, widthToFit, lengthToFit, heightToFit)){
                                    tempSpace.fitContainer(curContainer.getId(), widthToFit, lengthToFit, heightToFit, x, y, z);
                                    placedContainers.add(curContainer);
                                    Toast.makeText(context, curContainer.getLabel() + " fits", Toast.LENGTH_SHORT).show();
                                    break iterator;
                                }
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

    private boolean checkPossibleLocation(Space space, int x, int y, int z, int lengthToFit, int widthToFit, int heightToFit) {
        int remainingLength = lengthToFit;
        int remainingWidth = widthToFit;
        int remainingHeight = heightToFit;
        for (int tempX = x; tempX < space.getGridLength() && remainingLength > 0; tempX++){
            remainingWidth = widthToFit;
            for (int tempY = y; tempY < space.getGridWidth() && remainingWidth > 0; tempY++){
                remainingHeight = heightToFit;
                for(int tempZ = z; tempZ < space.getGridHeight() && remainingHeight > 0; tempZ++){
                    if (space.getSpaceCell(tempX, tempY, tempZ) == -1){
                        remainingHeight --;
                    } else {
                        return false;
                    }
                }
                remainingWidth --;
            }
            remainingLength --;
        }
        if(remainingLength > 0 || remainingWidth > 0 || remainingHeight > 0){
            return false;
        } else { return true; }
    }

    private boolean checkIfFits (Space space) {
        Integer totalCells = 0;
        for (Container curContainer : unplacedContainers) {
            boxCellLength = curContainer.lengthToCell();
            boxCellWidth = curContainer.widthToCell();
            boxCellHeight = curContainer.heightToCell();

            boxSize = boxCellLength * boxCellWidth * boxCellHeight;
            totalCells += boxSize;
        }
        if (totalCells > space.getAvailableCells()) {
            return false;
        } else {
            return true;
        }
    }
}
