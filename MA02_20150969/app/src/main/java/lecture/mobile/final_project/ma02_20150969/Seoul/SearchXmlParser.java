package lecture.mobile.final_project.ma02_20150969.Seoul;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import lecture.mobile.final_project.ma02_20150969.Food.FoodXmlParser;
import lecture.mobile.final_project.ma02_20150969.Food.Restaurant;

/**
 * Created by bkbk0 on 2017-12-26.
 */

public class SearchXmlParser {
    private final static String TAG = "FoodXmlParser";

    public enum TagType { NONE, TITLE, ADDRESS, CATEGORY, MENU, TEL };

    public SearchXmlParser() {

    }

    public ArrayList<Restaurant> parse(String xml, String menu) {
        ArrayList<Restaurant> resultList = new ArrayList();
        Restaurant dto = null;

        //enum 변수 초기화
        FoodXmlParser.TagType tagType = FoodXmlParser.TagType.NONE;

        try {
            //파서 준비
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            //태그 유형 구분 변수 준비
            int eventType = parser.getEventType();

            //parsing 수행
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("row")){
                            dto = new Restaurant();
                        }else if(parser.getName().equals("UPSO_NM")){
                            tagType = FoodXmlParser.TagType.TITLE;
                        }else if(parser.getName().equals("SITE_ADDR")) {
                            tagType = FoodXmlParser.TagType.ADDRESS;
                        } else if(parser.getName().equals("SNT_UPTAE_NM")){
                            tagType = FoodXmlParser.TagType.CATEGORY;
                        } else if(parser.getName().equals("MAIN_EDF")){
                            tagType = FoodXmlParser.TagType.MENU;
                        } else if(parser.getName().equals("UPSO_SITE_TELNO")){
                            tagType = FoodXmlParser.TagType.TEL;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("row")) {
                            if(menu.equals("null")){
                                resultList.add(dto);
                            }else{
                                if(dto.getMenu().equals(menu)) {
                                    resultList.add(dto);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case ADDRESS:
                                dto.setAddr(parser.getText());
                                break;
                            case TEL:
                                dto.setTel(parser.getText());
                                break;
                            case CATEGORY:
                                dto.setCategory(parser.getText());
                                break;
                            case MENU:
                                dto.setMenu(parser.getText());
                                break;
                        }
                        tagType = tagType.NONE;
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
