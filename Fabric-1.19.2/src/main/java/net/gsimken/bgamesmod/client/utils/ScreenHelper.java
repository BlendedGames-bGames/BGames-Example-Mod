package net.gsimken.bgamesmod.client.utils;


import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

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
    public int tittleOffset (Text text){
        return this.horizontalCenter-this.componentSize(text)/2;
    }

    public int pointsTextOffset(Text text){
        return this.backgroundWidth-this.componentSize(text)-5;
    }

    public int elementOffset(Text text, int row,int column){
        return this.leftPos+ this.rows.get(row).get(column)-this.componentSize(text)/2;
    }
    public int elementOffset(BGamesButton button, int row,int column){
        return  this.leftPos+ this.rows.get(row).get(column)-this.componentSize(button)/2;
    }
    public int elementOffset(int elementSize,  int row,int column){
        return this.leftPos+ this.rows.get(row).get(column)-elementSize/2;
    }
    public int labelOffSet(Text text, int row,int column){
        return this.rows.get(row).get(column)-this.componentSize(text)/2;
    }
    public int labelOffSet(int elementSize,  int row,int column){
        return  this.rows.get(row).get(column)-elementSize/2;
    }

    public void addRow(int elementsNewRow ){
        List<Integer> newRow = new ArrayList<>();
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
    public int componentSize(Text text){
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
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
    public static Text getPoints(int points){
        if(points == -1 || points == 1) {
            return  Text.translatable("gui.bgamesmod.point");
        }
        return Text.translatable("gui.bgameslibrary.display_attributes.points", Text.literal(String.valueOf(-1 * points)));
    }

}
