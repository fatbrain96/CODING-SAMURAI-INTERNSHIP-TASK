package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuizAPI {

    // Category mapping (name -> ID)
    private static final Map<String, String> CATEGORY_MAP = new HashMap<>();
    static {
        CATEGORY_MAP.put("general knowledge", "9");
        CATEGORY_MAP.put("books", "10");
        CATEGORY_MAP.put("film", "11");
        CATEGORY_MAP.put("music", "12");
        CATEGORY_MAP.put("theatre", "13");
        CATEGORY_MAP.put("television", "14");
        CATEGORY_MAP.put("video games", "15");
        CATEGORY_MAP.put("board games", "16");
        CATEGORY_MAP.put("science & nature", "17");
        CATEGORY_MAP.put("computers", "18");
        CATEGORY_MAP.put("mathematics", "19");
        CATEGORY_MAP.put("mythology", "20");
        CATEGORY_MAP.put("sports", "21");
        CATEGORY_MAP.put("geography", "22");
        CATEGORY_MAP.put("history", "23");
        CATEGORY_MAP.put("politics", "24");
        CATEGORY_MAP.put("art", "25");
        CATEGORY_MAP.put("celebrities", "26");
        CATEGORY_MAP.put("animals", "27");
        CATEGORY_MAP.put("vehicles", "28");
        CATEGORY_MAP.put("comics", "29");
        CATEGORY_MAP.put("gadgets", "30");
        CATEGORY_MAP.put("anime", "31");
        CATEGORY_MAP.put("cartoon & animations", "32");
    }

    // Public method used by GUI
    public static List<Question> fetchQuestions(int count, String category, String difficulty) {
        String apiUrl = "https://opentdb.com/api.php?amount=" + count;

        if (category != null && !category.isEmpty()) {
            String categoryId = CATEGORY_MAP.get(category.toLowerCase());
            if (categoryId != null) {
                apiUrl += "&category=" + categoryId;
            }
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            apiUrl += "&difficulty=" + URLEncoder.encode(difficulty, StandardCharsets.UTF_8);
        }
        apiUrl += "&type=multiple"; // only multiple choice

        JSONArray results = fetchQuestionsFromAPI(apiUrl);
        return parseQuestions(results);
    }

    // Low-level fetcher
    private static JSONArray fetchQuestionsFromAPI(String apiUrl) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getJSONArray("results");

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // Convert JSON results into List<Question>
    private static List<Question> parseQuestions(JSONArray results) {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);

            String questionText = decodeHtml(obj.getString("question"));
            String correctAnswer = decodeHtml(obj.getString("correct_answer"));

            // Collect incorrect answers
            List<String> options = new ArrayList<>();
            JSONArray incorrect = obj.getJSONArray("incorrect_answers");
            for (int j = 0; j < incorrect.length(); j++) {
                options.add(decodeHtml(incorrect.getString(j)));
            }
            options.add(correctAnswer);
            Collections.shuffle(options);

            Question q = new Question(questionText, options, correctAnswer);
            q.setCategory(obj.optString("category", ""));
            q.setType(obj.optString("type", ""));
            q.setDifficulty(obj.optString("difficulty", ""));
            q.setIncorrectAnswers(options); // to keep allAnswers in sync
            q.setCorrectAnswer(correctAnswer);

            questions.add(q);
        }

        return questions;
    }

    // Decode HTML entities
    public static String decodeHtml(String text) {
        if (text == null) return "";
        return text.replace("&quot;", "\"")
                   .replace("&#039;", "'")
                   .replace("&amp;", "&")
                   .replace("&lt;", "<")
                   .replace("&gt;", ">");
    }
}
