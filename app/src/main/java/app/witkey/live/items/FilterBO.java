package app.witkey.live.items;

/**
 * created by developer on 6/9/2017.
 */

public class FilterBO {
    String name;
    int path;
    int id;
    int type; /*1 FOR BEAUTY, 2 FOR STICKERS*/

    public FilterBO(String name_, int path_, int type_) {
        this.name = name_;
        this.path = path_;
        this.type = type_;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
