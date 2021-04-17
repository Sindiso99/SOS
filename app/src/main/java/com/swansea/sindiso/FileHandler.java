package com.swansea.sindiso;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class FileHandler {

    private Integer UserId;
    private Integer[][] floor;
    private Integer[][][] room;
    private StringBuilder stringBuilder;
    private BufferedReader bufferedReader;
    private Context context;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;
    private BufferedReader blockReader;
    private InputStreamReader blockStreamReader;
    private File file;

    public FileHandler(Context context) {
        this.context = context;
    }

//    public boolean writeFloorPlan(Space space) {
//        floor = space.getFloorLayout();
//        stringBuilder = new StringBuilder();
//        for(int x = 0; x < space.getGridLength(); x++){
//            for(int y = 0; y < space.getGridWidth(); y++){
//                stringBuilder.append(floor[x][y]+"");
//                if(y < space.getGridWidth() - 1) {
//                    stringBuilder.append(" ");
//                }
//            }
//            stringBuilder.append("\n");
//        }
//        try {
//            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Holder_space_Id_" + space.getOwnerId() + ".txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(stringBuilder.toString());
//            outputStreamWriter.close();
//            Toast.makeText(context, "Match Complete", Toast.LENGTH_SHORT).show();
//        }
//        catch (IOException e) {
//            Toast.makeText(context, "Failed to write to File", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

    public boolean writeFloorSpace(Space space) {
        room = space.getSpaceLayout();
        stringBuilder = new StringBuilder();
        for (int z = 0; z < space.getGridHeight(); z++) {
            for (int x = 0; x < space.getGridLength(); x++) {
                for (int y = 0; y < space.getGridWidth(); y++) {

                    stringBuilder.append(room[x][y][z]);
                    if (y < space.getGridWidth() - 1) {
                        stringBuilder.append(" ");
                    }
                }
                if (x < space.getGridLength() - 1) {
                    stringBuilder.append("\n");
                }
            }

            if (z < space.getGridWidth() - 1) {
                stringBuilder.append(",");
                stringBuilder.append("\n");
            }
        }
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Holder_3D_space_Id_" + space.getOwnerId() + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(stringBuilder.toString());
            outputStreamWriter.close();
            Toast.makeText(context, "Match Complete", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Failed to write to File", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public Integer[][][] readSpacePlan(Integer ownerId, int length, int width, int height) {
        room = new Integer[length][width][height];
        try {
            inputStream = context.openFileInput("Holder_3D_space_Id_" + ownerId + ".txt");
            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream);
                Scanner scanner = new Scanner(inputStreamReader);
                int slice = 0;
                String blocks = "";
                scanner.useDelimiter(",");
                while (scanner.hasNext() && slice < height) {
                    blocks = scanner.next();
                    Scanner blockScanner = new Scanner(blocks);
                    String curLine = "";
                    int row = 0;
                    while (blockScanner.hasNextLine() && row < length) {
                        curLine = blockScanner.nextLine();
                        if (curLine.equals("")){
                            curLine = blockScanner.nextLine();
                        }
                        String[] column = curLine.split(" ");
                        int col = 0;
                        for (String id : column) {
                            room[row][col][slice] = Integer.parseInt(id);
                            col++;
                        }
                        row++;
                    }
                    blockScanner.close();
                    slice++;
                }
                scanner.close();
            }
        } catch (IOException e) {
            Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
        }
        return room;
    }

//    public Integer[][] readFloorPlan(Integer ownerId, int length, int width) {
//        floor = new Integer[length][width];
//        try {
//           inputStream = context.openFileInput("Holder_space_Id_" + ownerId + ".txt");
//           if (inputStream != null) {
//               inputStreamReader = new InputStreamReader(inputStream);
//               bufferedReader = new BufferedReader(inputStreamReader);
//               String curLine = "";
//               int row = 0;
//               while((curLine = bufferedReader.readLine()) != null){
//                   String[] column = curLine.split(" ");
//                   int col = 0;
//                   for (String id : column) {
//                       floor[row][col] = Integer.parseInt(id);
//                       col++;
//                   }
//                   row++;
//               }
//               bufferedReader.close();
//           }
//        } catch (IOException e) {
//            Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
//        }
//        return floor;
//    }

    public boolean checkExistence(Integer ownerId) {
        file = context.getFileStreamPath("Holder_3D_space_Id_" + ownerId + ".txt");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
