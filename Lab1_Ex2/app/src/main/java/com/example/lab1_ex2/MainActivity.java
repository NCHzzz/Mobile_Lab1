package com.example.lab1_ex2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText sentenceInput;
    private TextView resultText;
    private ImageView emotionIcon;
    private static final String API_KEY = "AIzaSyCjSkcUU4Ls1ifLVaBdZmU0q3v2uv5hui8"; // Replace with your Gemini API key

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sentenceInput = findViewById(R.id.sentenceInput);
        resultText = findViewById(R.id.resultText);
        emotionIcon = findViewById(R.id.emotionIcon);
        Button classifyButton = findViewById(R.id.classifyButton);

        classifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentence = sentenceInput.getText().toString().trim();
                if (!sentence.isEmpty()) {
                    new SentimentAnalysisTask().execute(sentence);
                } else {
                    resultText.setText("Please enter a sentence.");
                    emotionIcon.setImageResource(0);
                }
            }
        });
    }

    private class SentimentAnalysisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String sentence = params[0];
            OkHttpClient client = new OkHttpClient();

            String prompt = "Classify the sentiment of this sentence as positive, negative, or neutral and return only one of these words: positive, negative, or neutral. Sentence: '" + sentence + "'";
            String jsonBody = "{\"contents\": [{\"parts\": [{\"text\": \"" + prompt + "\"}]}]}";

            RequestBody body = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    return json.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")
                            .trim()
                            .toLowerCase();
                } else {
                    return "error";
                }
            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            resultText.setText(result.equals("error") ? "Lỗi phân loại" : result);

            // Hiển thị icon tương ứng
            switch (result) {
                case "positive":
                    emotionIcon.setImageResource(R.drawable.ic_positive);
                    break;
                case "negative":
                    emotionIcon.setImageResource(R.drawable.ic_negative);
                    break;
                case "neutral":
                    emotionIcon.setImageResource(R.drawable.ic_neutral);
                    break;
                default:
                    emotionIcon.setImageResource(0);
                    break;
            }
        }
    }
}