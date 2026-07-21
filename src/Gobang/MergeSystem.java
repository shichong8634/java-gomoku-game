package Gobang;
//Yutian Wang
//3184472

public class MergeSystem {
    public static final int CLASSIC = 0;
    public static final int MERGE = 1;

    private Gomokulogic logic;

    public MergeSystem(Gomokulogic logic){
        this.logic = logic;
    }

    public void setClassic(){
        logic.gameMode = CLASSIC;
    }

    public void setMerge() {
        logic.gameMode = MERGE;
    }

    public int getMode() {
        return logic.gameMode;
    }
}
