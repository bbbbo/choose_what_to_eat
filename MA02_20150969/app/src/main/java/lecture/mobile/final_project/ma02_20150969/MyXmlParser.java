package lecture.mobile.final_project.ma02_20150969;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import lecture.mobile.final_project.ma02_20150969.Seoul.SimpleRes;

class MyXmlParser {

    public enum TagType { NONE, TITLE, ADDRESS, TEL };

    public MyXmlParser() {

    }

    public ArrayList<SimpleRes> parse(String xml) {
        ArrayList<SimpleRes> resultList = new ArrayList();
        SimpleRes dto = null;

        //enum 변수 초기화
        TagType tagType = TagType.NONE;

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
                        if(parser.getName().equals("item")){
                            dto = new SimpleRes();
                        }else if(parser.getName().equals("title")){
                            tagType = TagType.TITLE;
                        }else if(parser.getName().equals("tel")){
                            tagType = TagType.TEL;
                        }else if(parser.getName().equals("addr1")){
                            tagType = TagType.ADDRESS;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")) {
                            resultList.add(dto);
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
