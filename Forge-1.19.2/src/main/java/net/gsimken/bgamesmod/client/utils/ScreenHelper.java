package net.gsimken.bgamesmod.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;


import java.util.ArrayList;
import java.util.List;

public class ScreenHelper {
    int backgroundWidth;
    int horizontalCenter;
    int leftPos;
    List<List<Integer>> rows;
    public ScreenHelper(int backgroundWidth,int elementsFirstRow, int leftPos){
        this.backgroundWidth = backgroundWidth;
        this.horizontalCenter = this.backgroundWidth/2;
        this.rows = new ArrayList<>();
        this.leftPos = leftPos;
        addRow(elementsFirstRow);
    }
    public int tittleOffset (Component text){
        return this.horizontalCenter-this.componentSize(text)/2;
    }

    public int pointsTextOffset(Component text){
        return this.backgroundWidth-this.componentSize(text)-5;
    }

    public int elementOffset(Component text, int row,int column){
        return this.leftPos+ this.rows.get(row).get(column)-this.componentSize(text)/2;
    }
    public int elementOffset(BGamesButton button, int row,int column){
        return  this.leftPos+ this.rows.get(row).get(column)-this.componentSize(button)/2;
    }
    public int elementOffset(int elementSize,  int row,int column){
        return this.leftPos+ this.rows.get(row).get(column)-elementSize/2;
    }
    public int labelOffSet(Component text, int row,int column){
        return this.rows.get(row).get(column)-this.componentSize(text)/2;
    }
    public int labelOffSet(int elementSize,  int row,int column){
        return  this.rows.get(row).get(column)-elementSize/2;
    }

    public void addRow(int elementsNewRow ){
        List newRow = new ArrayList<>();
        int position;
        int spacing = (this.backgroundWidth/(elementsNewRow+1));
        for(int i=0;i<elementsNewRow;i++){
            position = spacing*(i+1);
            newRow.add(position);
        }
        this.rows.add(newRow);
    }

    /**
     * space an element to the rigth
     * @param text Minecraft tex component
     * @return size of the text
     * */
    public int componentSize(Component text){
        return Minecraft.getInstance().font.width(text);
    }
    /**
     * space an element to the rigth
     * @param button Minecraft tex component
     * @return size of the text
     * */
    public int componentSize(BGamesButton button){
        return button.getWidth();
    }
    /**
     * Fuction to converte the points in a string
     * @param points the points of the attibute
     * @return a string "X points", with transalation
     * */
    public static Component getPoints(int points){
        if(points == -1 || points == 1) {
            return  Component.translatable("gui.bgamesmod.point");
        }
        return Component.translatable("gui.bgameslibrary.display_attributes.points", Component.literal( String.valueOf(points)));
    }

}
