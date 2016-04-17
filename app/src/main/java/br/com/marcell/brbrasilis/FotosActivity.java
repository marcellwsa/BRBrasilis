package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;

public class FotosActivity extends Activity {

    int codigo;

    SQLiteDatabase db;

    Configuracoes configuracoes = new Configuracoes();

    String placa;
    String pathPrincipal;
    ImageView imageViewFrente;
    ImageView imageViewDireita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
        ReceberVariaveis();
        pathPrincipal = "/BRBrasilis/Fotos/"+codigo+"/"+placa;
        inicializaTelaFotos();
    }

    //pega as variaveis referentes as outras activitys. Codigo do acd e buttoneditar e quantidade de veiculos
    private void ReceberVariaveis() {
        try {
            Intent i = getIntent();
            codigo = i.getIntExtra("codigo", 1); //recebe o codigo que foi digitado na outra activity e o 1 seria o padrao caso nao tivesse sido definico o codigo
            placa= (i.getStringExtra("placa"));
        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro", "Erro ao inicializar variáveis de veículos." + e.toString());
        }
    } //fim do receberVariaveis

    private void inicializaTelaFotos (){
        setContentView(R.layout.activity_fotos);
        imageViewFrente = (ImageView) findViewById(R.id.imageViewFrente);
        imageViewDireita = (ImageView) findViewById(R.id.imageViewDireita);

        File imgFileFrente = new  File(Environment.getExternalStorageDirectory()+pathPrincipal+"-frente"+".jpg");
//        mostrarMensagemBasica("path", imgFileFrente.toString());

        if(imgFileFrente.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFileFrente.getAbsolutePath());
            imageViewFrente.setImageBitmap(myBitmap);
        }//fim do if para imageViewFrente

//        File imgFileDireita = new  File(Environment.getExternalStorageDirectory()+pathPrincipal+"-direita"+".jpg");
//        if(imgFileDireita.exists()) {
//            Bitmap myBitmapDireita = BitmapFactory.decodeFile(imgFileDireita.getAbsolutePath());
//            imageViewDireita.setImageBitmap(myBitmapDireita);
//        }//fim do if para imageViewDireita

    }

    public SQLiteDatabase abrirConexao () { //Abre a conexao com o BD
        try {
            db = openOrCreateDatabase(configuracoes.getNomeBanco(), Context.MODE_PRIVATE, null);
        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro!", "Erro ao abrir conexão com o BD: " +e.toString());
        }//fim do catch para abrir conexao
        return db;
    }//fim do metodo abrirConexao

    //mostrar a mensagem basica de confirmação de algo (erro, sucesso, etc).
    private void mostrarMensagemBasica(String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(title).setMessage(mensagem).setNeutralButton("OK", null).show();
    }//fim do metodo mostrarMensagemBasica

}
