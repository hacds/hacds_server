package diamond.util;

import com.alibaba.fastjson.JSONObject;
import diamond.dto.Diamond;
import diamond.dto.DiamondRule;
import diamond.service.DiamondDetailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Component
public class AnalyseDiamond {

    @Autowired
    private DiamondDetailService diamondDetailService;

    private static String shapes = "";

    private static String color = "";

    private static String styles = "";

    private static String nameliteral = "";

    private static String numberliteral = "";

    Map<String, DiamondRule> map = new HashMap<String, DiamondRule>();

    Map<String, DiamondRule> cleanMap = new HashMap<String, DiamondRule>();


    @PostConstruct
    public void init() {

        List<DiamondRule> rules = null;
        try {
            rules = readRuleFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rules.size());
        for (int i = 0; i < rules.size(); i++) {
            DiamondRule r = rules.get(i);
            cleanMap.put(r.getMetric_en().split("\\|")[0],r);
            map.put(r.getMetric_en(), r);
            if (r.getTypeEn().equals("shapes")) {
                shapes = shapes + rules.get(i).getMetric_en() + ",";
            }
            if (r.getTypeEn().equals("color")) {
                color = color + rules.get(i).getMetric_en() + ",";
            }
            if (r.getTypeEn().equals("styles")) {
                styles = styles + rules.get(i).getMetric_en() + ",";
            }
            if (r.getTypeEn().equals("nameliteral")) {
                nameliteral = nameliteral + rules.get(i).getMetric_en() + ",";
            }

            if (r.getTypeEn().equals("numberliteral")) {
                numberliteral = numberliteral + rules.get(i).getMetric_en() + ",";
            }

        }
        shapes = shapes.substring(0, shapes.length() - 1);
        color = color.substring(0, color.length() - 1);
        styles = styles.substring(0, styles.length() - 1);
        nameliteral = nameliteral.substring(0, nameliteral.length() - 1);
        numberliteral = numberliteral.substring(0, numberliteral.length() - 1);
        System.out.println(shapes);
        System.out.println(color);
        System.out.println(styles);
        System.out.println(numberliteral);
        System.out.println(nameliteral);

//        writeRuleToFile(JSONObject.toJSONString(map));


    }

    public List<DiamondRule> readRuleFromFile() throws IOException {
        List<DiamondRule> rules = new ArrayList<DiamondRule>();
        FileReader reader = new FileReader("rule.txt");
        BufferedReader bufr = new BufferedReader(reader);
        String line = bufr.readLine();
        JSONObject obj = JSONObject.parseObject(line);
        for(String key : obj.keySet()){
            DiamondRule rule = JSONObject.toJavaObject(obj.getJSONObject(key),DiamondRule.class);
            rules.add(rule);
        }
        return rules;

    }


    public Diamond analyseDiamond(Diamond d) {
        d.setNumberliteral(getNumberliterals(d));
        d.setNameliteral(getNameliteral(d));
        d.setShapes(getDiamondshapes(d));
        if (d.getShapes().equals("Common diamond")) {
            d.setStyles(getDiamondStyles(d));
        } else {
            d.setColor(getDiamondColor(d));
        }
        setDiamondScore(d);
        return d;
    }

    public void setDiamondScore(Diamond d){
        int shapeScore = cleanMap.get(d.getShapes())==null?0: cleanMap.get(d.getShapes()).getScore();
        int stylesScore = cleanMap.get(d.getStyles())==null?0: cleanMap.get(d.getStyles()).getScore();
        int nameLiteral = cleanMap.get(d.getNameliteral())==null?0: cleanMap.get(d.getNameliteral()).getScore();
        int numberLiteral = cleanMap.get(d.getNumberliteral())==null?0: cleanMap.get(d.getNumberliteral()).getScore();
        int color =  cleanMap.get(d.getColor())==null?0: cleanMap.get(d.getColor()).getScore();
        d.setScore(shapeScore+stylesScore+nameLiteral+numberLiteral+color);
    }



    public String getDiamondStyles(Diamond d) {
        StringBuffer sb = new StringBuffer();
//        String s = d.getVisualGene().substring(2,d.getVisualGene().length());
        String style = d.getVisualGene().substring(2, 18);
        String thirteen = style.charAt(12) + "";
        String fourteen = style.charAt(13) + "";
        String fiveteen = style.charAt(14) + "";
        String sixteen = style.charAt(15) + "";

        if (thirteen.equals(fourteen) && fourteen.equals(fiveteen) && fiveteen.equals(sixteen)) {
            sb.append("Pure,");
        }
        if ((fourteen.equals(fiveteen) && fiveteen.equals(thirteen)) && !fourteen.equals(sixteen)) {
            sb.append("Left three pure,");
        }
        if ((thirteen.equals(fiveteen) && fiveteen.equals(sixteen)) && !fourteen.equals(thirteen)) {
            sb.append("Left mix pure,");
        }
        if ((thirteen.equals(fourteen) && fourteen.equals(sixteen)) && !fiveteen.equals(thirteen)) {
            sb.append("Right three pure,");
        }
        if ((thirteen.equals(fourteen) && sixteen.equals(fiveteen)) && !fiveteen.equals(thirteen)) {
            sb.append("Right mix pure ,");
        }
        if (sixteen.equals(fiveteen) && thirteen.equals(fourteen)) {
            sb.append("Symmetry,");
        }
        if (sixteen.equals(fourteen) && thirteen.equals(fiveteen)) {
            sb.append("Half divide,");
        }
        if ((fiveteen + thirteen).equals(fourteen + sixteen)) {
            sb.append("Double mix,");
        }
        if (thirteen.equals(fourteen) &&  !fiveteen.equals(sixteen)) {
            sb.append("Center color,");
        }
        if (fiveteen.equals(sixteen) && !fourteen.equals(thirteen)) {
            sb.append("Edge color,");
        }
        if (distinctChar(style) == 4) {
            sb.append("Sum four color,");
        }
        if (distinctChar(style) == 5) {
            sb.append("Sum five color,");
        }
        if (distinctChar(style) == 6) {
            sb.append("Sum six color,");
        }
        if (distinctChar(style) == 7) {
            sb.append("Sum seven color ,");
        }
        if (distinctChar(style) == 14) {
            sb.append("All 14 color,");
        }
        if (distinctChar(style) == 15) {
            sb.append("All 15 color,");
        }
        if (distinctChar(style) == 16) {
            sb.append("All 16 color,");
        }
        if (darkOrLight(style).equals("1")) {
            sb.append("darkColor,");
        }
        if (darkOrLight(style).equals("0")) {
            sb.append("lightColor,");
        }

        if (sb.length() == 0) {
            return "Common diamond";
        } else {
            return getMaxScoreFeature(sb.toString().substring(0, sb.length() - 1));
        }
    }


    public String darkOrLight(String styles) {

        int flag = 0;
        String darkColor = "01234";
        String lightColor = "56789abcdef";
        if (darkColor.contains(styles.charAt(0) + "")) {
            flag = 1;
        }
        if (lightColor.contains(styles.charAt(0) + "")) {
            flag = 0;
        }
        char[] chars = styles.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            if (flag == 1 && lightColor.contains(chars[i] + "")) {
                  return "2";
            }
            if (flag == 0 && darkColor.contains(chars[i] + "")) {
                return "2";
            }
        }
        if (flag == 1) {
            return "1";
        }
        if (flag == 0) {
            return "0";
        }
        return "2";
    }


    public String getDiamondshapes(Diamond diamond) {
        String visualGene = diamond.getVisualGene();
        String[] shapesArray = shapes.split(",");
        for (int i = 0; i < shapesArray.length; i++) {
            if (shapesArray[i].split("\\|")[1].equals(visualGene.substring(0, 2))) {
                return shapesArray[i].split("\\|")[0];
            }
        }
        return "Common diamond";
    }

    public String getDiamondColor(Diamond diamond) {
        String style = diamond.getVisualGene().substring(2, 19);
        StringBuffer sb = new StringBuffer();
        String name = diamond.getName();
        String[] colorArray = color.split(",");
        for (int i = 0; i < colorArray.length; i++) {
            if (colorArray[i].split("\\|").length > 1) {
                if (colorArray[i].split("\\|")[1].equals(name.charAt(0) + "")) {
                    sb.append(colorArray[i] + ",");
                }
            }

        }
        if (darkOrLight(style).equals("1")) {
            sb.append("darkColor|color,");
        }
        if (darkOrLight(style).equals("0")) {
            sb.append("lightColor|color,");
        }
        if (sb.length() == 0) {
            return "Common diamond";
        } else {
            String s = getMaxScoreFeature(sb.toString().substring(0, sb.length() - 1));
            return s.split("\\|")[0];
        }
    }

    public String getNumberliterals(Diamond diamond) {
        String name = diamond.getName();
        String number = diamond.getNumber();
        StringBuffer sb = new StringBuffer();
        String[] array = numberliteral.split(",");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("Single num")) {
                if (Integer.parseInt(number) < 10) {
                    sb.append("Single num,");
                }
            }
            if (array[i].equals("Repetition")) {
                if (distinctChar(number) == 1) {
                    sb.append("Repetition,");
                }
            }
            if (array[i].equals("Serial num")) {
                if (shunzi(number, 1) || shunzi(number, -1)) {
                    sb.append("Serial num,");
                }
            }
            if (array[i].equals("Tail seven")) {
                if (repeat(number) == 7) {
                    sb.append("Tail seven,");
                }
            }
            if (array[i].equals("Tail six")) {
                if (repeat(number) == 6) {
                    sb.append("Tail six,");
                }
            }
            if (array[i].equals("Tail five")) {
                if (repeat(number) == 5) {
                    sb.append("Tail five,");
                }
            }
            if (array[i].equals("Tail four")) {
                if (repeat(number) == 4) {
                    sb.append("Tail four,");
                }
            }

//            if (array[i].equals("编号对称")) {
//                if (symmetry(number)) {
//                    sb.append("编号对称,");
//                }
//            }
//            if (array[i].equals("串行重复")) {
//                if (serialRepeat(number, 1) || serialRepeat(number, -1)) {
//                    sb.append("串行重复,");
//                }
//            }


        }

        if (sb.length() == 0) {
            return "Common diamond";
        } else {
            return getMaxScoreFeature(sb.toString().substring(0, sb.length() - 1));
        }

    }


    public String getNameliteral(Diamond diamond) {
        String name = diamond.getName();
        StringBuffer sb = new StringBuffer();
        String[] array = nameliteral.split(",");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("six repeat")) {//
                if (repeat(name) == 6) {
                    sb.append("six repeat,");
                }
            }
            if (array[i].equals("Penta repeat")) {//
                if (repeat(name) == 5) {
                    sb.append("Penta repeat,");
                }
            }
            if (array[i].equals("Quadro repeat")) {//
                if (repeat(name) == 4) {
                    sb.append("Quadro repeat,");
                }
            }
            if (array[i].equals("Triple repeat")) {//
                if (repeat(name) == 3) {
                    sb.append("Triple repeat,");
                }
            }
            if (array[i].equals("Two letters")) {//
                if (distinctChar(name) == 2) {
                    sb.append("Two letters,");
                }
            }
            if (array[i].equals("Three letters")) {//
                if (distinctChar(name) == 3) {
                    sb.append("Three letters,");
                }
            }
            if (array[i].equals("Half repeat")) { //
                if (name.substring(0, 3).equals(name.substring(3, 6))) {
                    sb.append("Half repeat,");
                }
            }
            if (array[i].equals("Symmetric letter")) {
                if (symmetry(name)) {
                    sb.append("Symmetric letter,");
                }
            }
            if(array[i].equals("AABCCD")){
                if(name.charAt(0)==name.charAt(1) && name.charAt(3)==name.charAt(4)){
                    sb.append("AABCCD,");
                }

            }
            if(array[i].equals("ABBCDD")){
                if(name.charAt(1)==name.charAt(2) && name.charAt(4)==name.charAt(5)){
                    sb.append("ABBCDD,");
                }

            }

        }

        if (sb.length() == 0) {
            return "Common diamond";
        } else {
            return getMaxScoreFeature(sb.toString().substring(0, sb.length() - 1));
        }

    }

    public static boolean symmetry(String s) {
        String s1 = new StringBuffer(s).reverse().toString();
        int mid = s.length() / 2;
        return s.substring(0, mid).equals(s1.substring(0, mid));

    }


    public static int repeat(String s) {
        int max = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            int j = i;
            int count = 1;
            while (j < s.length() - 1 && s.charAt(j) == s.charAt(j + 1)) {
                count++;
                j++;
            }
            i = j;
            max = Math.max(max, count);
        }
        return max;
    }

    public static int distinctChar(String s) {
        Set set = new HashSet();
        char[] a = s.toCharArray();
        for (int i = 0; i < a.length; i++) {
            set.add(a[i]);
        }
        return set.size();
    }

    public static boolean shunzi(String number, int cha) {
        char[] array = number.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                continue;
            } else {
                if (Integer.parseInt(array[i + 1] + "") - Integer.parseInt(array[i] + "") != cha) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean serialRepeat(String number, int cha) {
        char[] array = number.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                continue;
            } else {
                if (Integer.parseInt(array[i + 1] + "") - Integer.parseInt(array[i] + "") != cha) {
                    if (Integer.parseInt(array[i + 1] + "") - Integer.parseInt(array[i] + "") != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getMaxScoreFeature(String s) {

        String[] features = s.split(",");
        List<DiamondRule> list = new ArrayList<DiamondRule>();
        for (int i = 0; i < features.length; i++) {
            list.add(map.get(features[i]));
        }
        list.sort((Comparator<DiamondRule>) (o1, o2) -> o2.getScore() - o1.getScore());
        return list.get(0).getMetric_en();
    }


    @Test
    public void main() {

        String s = "123456789123456789";
        StringBuffer s1 = new StringBuffer(s);
        System.out.println(s.substring(2, 18));

    }


}
