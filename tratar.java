import java.util.ArrayList;
import java.util.List;

import netscape.javascript.JSObject;

public class tratar {

    public static void main(String[] args) {
        System.out.println(jsonToAPKList().get(1).name);


        runtime
    }

    public static List<Apk> jsonToAPKList() {
        String[] regex = { "{", "[", "]", "}" };
        String toTrate = "[{'apk':'yukaplay','version':'1.6.4'},{'apk':'youtube','version':'5.6.4555s'}]";
        toTrate = toTrate.replaceAll("'", "");
        toTrate = toTrate.replaceAll("\"", "");
        String[] splited = toTrate.split("},");

        String apk = "nao encontrado";
        String version = "nao encontrado";

        List<Apk> apkFreshList = new ArrayList<>();

        for (String s : splited) {
            String jsonName = "";
            String jsonVersion = "";

            String[] apkSplited = s.split(",");

            jsonName = (apkSplited[0].split(":")[1].replace("}]", ""));
            jsonVersion = (apkSplited[1].split(":")[1].replace("}]", ""));

            System.out.println(jsonName + "\n" + jsonVersion);
            System.out.println("----------------");
            Apk newApk = new Apk(jsonName, jsonVersion);
            apkFreshList.add(newApk);
        }

        return apkFreshList;
    }

    public static class Apk {
        String name = "";
        String version = "0.0";

        public Apk(String name, String version) {
            this.name = name;
            this.version = version;
        }

    }

}