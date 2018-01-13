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

public class RecipeXmlParser {

    public enum TagType { NONE, ID, NO, COMMENT };

    public RecipeXmlParser() {

    }

    public ArrayList<Recipe> parse(String xml) {
        ArrayList<Recipe> resultList = new ArrayList();
        Recipe dto = null;

        //enum 변수 초기화
        RecipeXmlParser.TagType tagType = RecipeXmlParser.TagType.NONE;

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
                            dto = new  Recipe();
                        }else if(parser.getName().equals("RECIPE_ID")){
                            tagType =  RecipeXmlParser.TagType.ID;
                        } else if(parser.getName().equals("COOKING_NO")){
                            tagType =  RecipeXmlParser.TagType.NO;
                        }else if(parser.getName().equals("COOKING_DC")) {
                            tagType =  TagType.COMMENT;
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
                            case NO:
                                dto.setNo(parser.getText());
                                break;
                            case COMMENT:
                                dto.setComment(parser.getText());
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
