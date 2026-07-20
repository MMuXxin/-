package RandomGenerator2.Tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static RandomGenerator2.RT2.currentLang;

public class SimpleWeatherApp {

    public SimpleWeatherApp() {
    }

    private static final Map<String, String> zhTranslationMap = new HashMap<>();
    private static final Map<String, String> jpTranslationMap = new HashMap<>();

    static {
        zhTranslationMap.put("FeelsLikeC", "体感温度(℃)");
        zhTranslationMap.put("FeelsLikeF", "体感温度(℉)");
        zhTranslationMap.put("cloudcover", "云量(%)");
        zhTranslationMap.put("humidity", "湿度(%)");
        zhTranslationMap.put("observation_time", "观测时间");
        zhTranslationMap.put("precipInches", "降水量(英寸)");
        zhTranslationMap.put("precipMM", "降水量(毫米)");
        zhTranslationMap.put("pressure", "气压(百帕)");
        zhTranslationMap.put("pressureInches", "气压(英寸)");
        zhTranslationMap.put("temp_C", "当前温度(℃)");
        zhTranslationMap.put("temp_F", "当前温度(℉)");
        zhTranslationMap.put("tempC", "温度(℃)");
        zhTranslationMap.put("tempF", "温度(℉)");
        zhTranslationMap.put("uvIndex", "紫外线指数");
        zhTranslationMap.put("visibility", "能见度(公里)");
        zhTranslationMap.put("visibilityMiles", "能见度(英里)");
        zhTranslationMap.put("weatherCode", "天气代码");
        zhTranslationMap.put("weatherDesc", "天气描述");
        zhTranslationMap.put("weatherIconUrl", "天气图标");
        zhTranslationMap.put("winddir16Point", "风向(16方位)");
        zhTranslationMap.put("winddirDegree", "风向角度");
        zhTranslationMap.put("windspeedKmph", "风速(km/h)");
        zhTranslationMap.put("windspeedMiles", "风速(英里/h)");
        zhTranslationMap.put("nearest_area", "最近区域");
        zhTranslationMap.put("areaName", "地名");
        zhTranslationMap.put("country", "国家");
        zhTranslationMap.put("latitude", "纬度");
        zhTranslationMap.put("longitude", "经度");
        zhTranslationMap.put("population", "人口");
        zhTranslationMap.put("region", "省份/地区");
        zhTranslationMap.put("weatherUrl", "天气网址");
        zhTranslationMap.put("request", "请求信息");
        zhTranslationMap.put("query", "查询内容");
        zhTranslationMap.put("type", "类型");
        zhTranslationMap.put("weather", "天气预报");
        zhTranslationMap.put("astronomy", "天文数据");
        zhTranslationMap.put("moon_illumination", "月球照明度(%)");
        zhTranslationMap.put("moon_phase", "月相");
        zhTranslationMap.put("moonrise", "月升时间");
        zhTranslationMap.put("moonset", "月落时间");
        zhTranslationMap.put("sunrise", "日出时间");
        zhTranslationMap.put("sunset", "日落时间");
        zhTranslationMap.put("avgtempC", "平均温度(℃)");
        zhTranslationMap.put("avgtempF", "平均温度(℉)");
        zhTranslationMap.put("date", "日期");
        zhTranslationMap.put("hourly", "每小时预报");
        zhTranslationMap.put("DewPointC", "露点(℃)");
        zhTranslationMap.put("DewPointF", "露点(℉)");
        zhTranslationMap.put("HeatIndexC", "酷热指数(℃)");
        zhTranslationMap.put("HeatIndexF", "酷热指数(℉)");
        zhTranslationMap.put("WindChillC", "风寒指数(℃)");
        zhTranslationMap.put("WindChillF", "风寒指数(℉)");
        zhTranslationMap.put("WindGustKmph", "阵风风速(km/h)");
        zhTranslationMap.put("WindGustMiles", "阵风风速(英里/h)");
        zhTranslationMap.put("chanceoffog", "起雾概率(%)");
        zhTranslationMap.put("chanceoffrost", "结霜概率(%)");
        zhTranslationMap.put("chanceofhightemp", "高温概率(%)");
        zhTranslationMap.put("chanceofovercast", "阴天概率(%)");
        zhTranslationMap.put("chanceofrain", "降雨概率(%)");
        zhTranslationMap.put("chanceofremdry", "保持干燥概率(%)");
        zhTranslationMap.put("chanceofsnow", "降雪概率(%)");
        zhTranslationMap.put("chanceofsunshine", "晴天概率(%)");
        zhTranslationMap.put("chanceofthunder", "雷暴概率(%)");
        zhTranslationMap.put("chanceofwindy", "大风概率(%)");
        zhTranslationMap.put("diffRad", "散射辐射");
        zhTranslationMap.put("shortRad", "短波辐射");
        zhTranslationMap.put("time", "时间点");
        zhTranslationMap.put("value", "内容");
        zhTranslationMap.put("maxtempC", "最高温度(℃)");
        zhTranslationMap.put("maxtempF", "最高温度(℉)");
        zhTranslationMap.put("mintempC", "最低温度(℃)");
        zhTranslationMap.put("mintempF", "最低温度(℉)");
        zhTranslationMap.put("sunHour", "日照时长");
        zhTranslationMap.put("totalSnow_cm", "总降雪(厘米)");

        jpTranslationMap.put("FeelsLikeC", "体感温度(℃)");
        jpTranslationMap.put("FeelsLikeF", "体感温度(℉)");
        jpTranslationMap.put("cloudcover", "雲量(%)");
        jpTranslationMap.put("humidity", "湿度(%)");
        jpTranslationMap.put("observation_time", "観測時間");
        jpTranslationMap.put("precipInches", "降水量(インチ)");
        jpTranslationMap.put("precipMM", "降水量(ミリ)");
        jpTranslationMap.put("pressure", "気圧(ヘクトパスカル)");
        jpTranslationMap.put("pressureInches", "気圧(インチ)");
        jpTranslationMap.put("temp_C", "現在の気温(℃)");
        jpTranslationMap.put("temp_F", "現在の気温(℉)");
        jpTranslationMap.put("tempC", "気温(℃)");
        jpTranslationMap.put("tempF", "気温(℉)");
        jpTranslationMap.put("uvIndex", "紫外線指数");
        jpTranslationMap.put("visibility", "視程(キロメートル)");
        jpTranslationMap.put("visibilityMiles", "視程(マイル)");
        jpTranslationMap.put("weatherCode", "天気コード");
        jpTranslationMap.put("weatherDesc", "天気概要");
        jpTranslationMap.put("weatherIconUrl", "天気アイコン");
        jpTranslationMap.put("winddir16Point", "風向(16方位)");
        jpTranslationMap.put("winddirDegree", "風向角度");
        jpTranslationMap.put("windspeedKmph", "風速(km/h)");
        jpTranslationMap.put("windspeedMiles", "風速(マイル/h)");
        jpTranslationMap.put("nearest_area", "最寄り地域");
        jpTranslationMap.put("areaName", "地名");
        jpTranslationMap.put("country", "国");
        jpTranslationMap.put("latitude", "緯度");
        jpTranslationMap.put("longitude", "経度");
        jpTranslationMap.put("population", "人口");
        jpTranslationMap.put("region", "省/地域");
        jpTranslationMap.put("weatherUrl", "天気URL");
        jpTranslationMap.put("request", "リクエスト情報");
        jpTranslationMap.put("query", "クエリ内容");
        jpTranslationMap.put("type", "タイプ");
        jpTranslationMap.put("weather", "天気予報");
        jpTranslationMap.put("astronomy", "天文データ");
        jpTranslationMap.put("moon_illumination", "月面照度(%)");
        jpTranslationMap.put("moon_phase", "月相");
        jpTranslationMap.put("moonrise", "月の出時刻");
        jpTranslationMap.put("moonset", "月の入り時刻");
        jpTranslationMap.put("sunrise", "日の出時刻");
        jpTranslationMap.put("sunset", "日の入り時刻");
        jpTranslationMap.put("avgtempC", "平均気温(℃)");
        jpTranslationMap.put("avgtempF", "平均気温(℉)");
        jpTranslationMap.put("date", "日付");
        jpTranslationMap.put("hourly", "毎時の予報");
        jpTranslationMap.put("DewPointC", "露点温度(℃)");
        jpTranslationMap.put("DewPointF", "露点温度(℉)");
        jpTranslationMap.put("HeatIndexC", "暑さ指数(℃)");
        jpTranslationMap.put("HeatIndexF", "暑さ指数(℉)");
        jpTranslationMap.put("WindChillC", "風冷指数(℃)");
        jpTranslationMap.put("WindChillF", "風冷指数(℉)");
        jpTranslationMap.put("WindGustKmph", "突風風速(km/h)");
        jpTranslationMap.put("WindGustMiles", "突風風速(マイル/h)");
        jpTranslationMap.put("chanceoffog", "霧発生確率(%)");
        jpTranslationMap.put("chanceoffrost", "霜発生確率(%)");
        jpTranslationMap.put("chanceofhightemp", "高温発生確率(%)");
        jpTranslationMap.put("chanceofovercast", "曇天確率(%)");
        jpTranslationMap.put("chanceofrain", "降雨確率(%)");
        jpTranslationMap.put("chanceofremdry", "乾燥維持確率(%)");
        jpTranslationMap.put("chanceofsnow", "降雪確率(%)");
        jpTranslationMap.put("chanceofsunshine", "晴天確率(%)");
        jpTranslationMap.put("chanceofthunder", "雷発生確率(%)");
        jpTranslationMap.put("chanceofwindy", "強風確率(%)");
        jpTranslationMap.put("diffRad", "散乱放射");
        jpTranslationMap.put("shortRad", "短波放射");
        jpTranslationMap.put("time", "時刻");
        jpTranslationMap.put("value", "内容");
        jpTranslationMap.put("maxtempC", "最高気温(℃)");
        jpTranslationMap.put("maxtempF", "最高気温(℉)");
        jpTranslationMap.put("mintempC", "最低気温(℃)");
        jpTranslationMap.put("mintempF", "最低気温(℉)");
        jpTranslationMap.put("sunHour", "日照時間");
        jpTranslationMap.put("totalSnow_cm", "総降雪量(センチ)");
    }

    public static String translateLog(String input,Map<String,String> langMap) {
        Pattern pattern = Pattern.compile("([a-zA-Z_0-9]+):");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String translation = langMap.getOrDefault(key, key);
            matcher.appendReplacement(sb, translation + ":");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static Map<String, String> getWeatherMapss(String city, int after) {
        String url = "https://wttr.in/" + city + "?format=j1";
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String s = response.body().replaceAll("[\"{}\\[\\]]", "")
                        .replaceAll(":", ": ")
                        .replaceAll(",", "\n")
                        .replaceAll("(?m)^\\s+", "")
                        .trim();
                int currentLanguage = currentLang;
                Map<String, String> map = currentLanguage == 1 ? zhTranslationMap : jpTranslationMap;
                String s1 = translateLog(s, map);
                Map<String, String> weatherMapss = new HashMap<>();
                String[] splits = s1.split("\\n");

                int dayCounter = -1;

                String astronomyKey = currentLanguage == 1 ? "天文数据" : "天文データ";

                for (int i = 0; i < splits.length; i++) {
                    String[] kv = splits[i].split(":", 2);
                    if (kv.length == 2) {
                        String k = kv[0].trim();
                        String v = kv[1].trim();

                        if (k.equals(astronomyKey)) {
                            dayCounter++;
                        }

                        if (!k.isEmpty() && !v.isEmpty() && !k.equals("内容")) {
                            if (dayCounter == -1 && after == 0) {
                                weatherMapss.put(k, v);
                            } else if (dayCounter == after) {
                                weatherMapss.put(k, v);
                            }
                        }
                    }
                }
                return weatherMapss;
            } else {
                return null;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }}