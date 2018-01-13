package lecture.mobile.final_project.ma02_20150969.cook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by bkbk0 on 2017-12-29.
 */

public class CookXmlParser {
    private final static String TAG = "FoodXmlParser";

    public enum TagType { NONE, ID, NAME, SUMMARY, TIME, IMAGE };

    public CookXmlParser() {

    }

    public ArrayList<Cook> parse(String xml) {
        ArrayList<Cook> resultList = new ArrayList();
        Cook dto = null;

        //enum 변수 초기화
        CookXmlParser.TagType tagType = CookXmlParser.TagType.NONE;

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
                            dto = new Cook();
                        }else if(parser.getName().equals("RECIPE_ID")){
                            tagType = CookXmlParser.TagType.ID;
                        } else if(parser.getName().equals("RECIPE_NM_KO")){
                            tagType = CookXmlParser.TagType.NAME;
                        }else if(parser.getName().equals("SUMRY")) {
                            tagType = CookXmlParser.TagType.SUMMARY;
                        } else if(parser.getName().equals("COOKING_TIME")){
                            tagType = CookXmlParser.TagType.TIME;
                        } else if(parser.getName().equals("IMG_URL")){
                            tagType = CookXmlParser.TagType.IMAGE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("row")) {
                            resultList.add(dto);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case ID:
                                dto.setRecipe_id(parser.getText());
                                break;
                            case NAME:
                                dto.setName(parser.getText());
                                break;
                            case SUMMARY:
                                dto.setSummary(parser.getText());
                                break;
                            case TIME:
                                dto.setTime(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImage(parser.getText());
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
