package lecture.mobile.final_project.ma02_20150969.Food;

/**
 * Created by bkbk0 on 2017-12-14.
 */

public class Restaurant {
    private String addr;    //음식점 주소
    private String title;   //음식점 이름
    private String tel;     //음식점 전화번호
    private String category;
    private String menu;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    @Override
    public String toString() {
        return title + "\n"
               + addr + "\n" + category + " / " + menu + "\n"
                 + tel;
    }
}
