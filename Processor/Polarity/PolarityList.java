package Processor.Polarity;

public class PolarityList {
    StringBuilder polarity;
    int counter = 0;

    PolarityList(){
        this.polarity = new StringBuilder();
    }
    /***
     * Assigns a (sub-)/formula a new name for the structure preserving translation.
     *
     * The names will start with P_F,P_1,P_2,P_3,P_4,...
     */
    String inventName(){
        String prefix = "P_";
        String suffix;
        String newName;
        if (counter == 0){
            suffix = "F";
        }
        else if(counter >= 0){
            suffix = Integer.toString(counter);
        }
        else{
            throw new IllegalStateException("The counter can not be negative");
        }
        newName = prefix + suffix;
        counter ++;
        return newName;
    }

    void append(String i){
        polarity.append(i);
    }

    public String toString(){
        return polarity.toString();
    }


}
