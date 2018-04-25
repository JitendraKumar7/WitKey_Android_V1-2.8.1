package app.witkey.live.items;

public class ViewersSummaryBO {

    private String ImageURL;
    private String name;
    private int rankLocalImage;
    private String rankingNumber;
    private int cashLocalImage;
    private String cash;

    public ViewersSummaryBO(String imageURL, String name, int rankLocalImage, String rankingNumber, int cashLocalImage, String cash) {
        ImageURL = imageURL;
        this.name = name;
        this.rankLocalImage = rankLocalImage;
        this.rankingNumber = rankingNumber;
        this.cashLocalImage = cashLocalImage;
        this.cash = cash;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRankLocalImage() {
        return rankLocalImage;
    }

    public void setRankLocalImage(int rankLocalImage) {
        this.rankLocalImage = rankLocalImage;
    }

    public String getRankingNumber() {
        return rankingNumber;
    }

    public void setRankingNumber(String rankingNumber) {
        this.rankingNumber = rankingNumber;
    }

    public int getCashLocalImage() {
        return cashLocalImage;
    }

    public void setCashLocalImage(int cashLocalImage) {
        this.cashLocalImage = cashLocalImage;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
