package app.witkey.live.items;

public class HowToLevelUpBO {

    String levelUpProcedure;
    String levelUpReward;

    public HowToLevelUpBO(String levelUpProcedure, String levelUpReward) {
        this.levelUpProcedure = levelUpProcedure;
        this.levelUpReward = levelUpReward;
    }

    public String getLevelUpProcedure() {
        return levelUpProcedure;
    }

    public void setLevelUpProcedure(String levelUpProcedure) {
        this.levelUpProcedure = levelUpProcedure;
    }

    public String getLevelUpReward() {
        return levelUpReward;
    }

    public void setLevelUpReward(String levelUpReward) {
        this.levelUpReward = levelUpReward;
    }
}
