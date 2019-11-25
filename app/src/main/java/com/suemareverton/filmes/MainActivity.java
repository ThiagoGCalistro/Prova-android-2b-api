package com.suemareverton.filmes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private EditText nomeEditText;
    private Button pesquisarButton;
    private ProgressBar progressBar;
    private TextView resultadoTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeEditText = (EditText)
                findViewById(R.id.nome_edittext);
        pesquisarButton = (Button)
                findViewById(R.id.pesquisar_button);
        progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        resultadoTextView = (TextView)
                findViewById(R.id.resultado_textview);
        posterImageView = (ImageView)
                findViewById(R.id.poster_imageview);

        posterImageView.setImageBitmap(null);

        limparTela();
    }


    public void pesquisar(View v) {
        limparTela();
        pesquisarButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String url =
                "http://www.omdbapi.com/?t=" +
                        nomeEditText.getText().toString().replace(' ','+');

        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBar.setVisibility(View.INVISIBLE);
                        pesquisarButton.setEnabled(true);

                        if(e != null) {
                            // Ocorreu algum erro
                            resultadoTextView.setText("Erro: " + e.getMessage());
                        }
                        else {
                            String diretor =
                                    result.get("Director").getAsString();

                            String sinopse =
                                    result.get("Plot").getAsString();

                            JsonArray ratings =
                                    result.get("Ratings").getAsJsonArray();

                            String valor =
                                    ratings.getAsJsonArray().get(2).getAsJsonObject().get("Value").getAsString();

                            resultadoTextView.setText("Director: " + diretor + "\n\n" + "Sinopse: " + sinopse);

                            String poster =
                                    result.get("Poster").getAsString();

                            Picasso.with(MainActivity.this)
                                    .load(poster)
                                    .into(posterImageView);

                            posterImageView.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private void limparTela() {
        progressBar.setVisibility(View.INVISIBLE);
        posterImageView.setVisibility(View.INVISIBLE);
        resultadoTextView.setText("");
    }
}
