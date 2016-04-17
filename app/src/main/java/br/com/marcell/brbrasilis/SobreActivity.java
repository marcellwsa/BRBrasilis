package br.com.marcell.brbrasilis;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SobreActivity extends Activity {

    ImageView imageViewEmail, imageViewWhatsapp;
    TextView textViewNovasVersoes;

    Configuracoes configuracoes = new Configuracoes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarregaTelaSobre();
    }

    private void CarregaTelaSobre () {
        setContentView(R.layout.activity_sobre);
        imageViewEmail = (ImageView) findViewById(R.id.imageViewEmail);
        imageViewWhatsapp = (ImageView) findViewById(R.id.imageViewWhatsapp);
        textViewNovasVersoes = (TextView) findViewById(R.id.textViewNovasVersoes);

        imageViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarEmail();
            }
        });

        imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarWhatsapp();
            }
        });
        textViewNovasVersoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirNovasVersoes(configuracoes.getSiteAtualizacaoAPK());
                ClipboardManager texto = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text",configuracoes.getSiteAtualizacaoAPK());
                texto.setPrimaryClip(clip);
                Toast.makeText(SobreActivity.this, "Site copiado: " + configuracoes.getSiteAtualizacaoAPK(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void enviarEmail () {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "marcell.wendling@prf.gov.br", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Programa BRBrasilis");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Seu texto");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void enviarWhatsapp () {
        String uri = "tel:" + "6896049066" ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void abrirNovasVersoes (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}
