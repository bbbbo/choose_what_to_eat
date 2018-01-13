package lecture.mobile.final_project.ma02_20150969.cook;

/**
 * Created by bkbk0 on 2017-12-29.
 */

public class Cook {
    private int _id;
    private String recipe_id;
    private String name;
    private String summary;
    private String time;
    private String image;


    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Cook: " + recipe_id + "\n이름" + name + "\n요약:" + summary
                + "\n시간:" + time + "\n이미지:" + image;
    }
}
