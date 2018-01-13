package lecture.mobile.final_project.ma02_20150969.cook;

/**
 * Created by bkbk0 on 2017-12-29.
 */

public class Recipe {
    private int _id;
    private String recipe_id;
    private String no;
    private String comment;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return no + ". " + comment;
    }
}
