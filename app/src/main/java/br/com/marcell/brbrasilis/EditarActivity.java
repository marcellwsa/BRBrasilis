package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditarActivity extends Activity {

    int codigo;
    Boolean contadorButtonEditar= false;

    SQLiteDatabase db;
    Cursor c;

    Configuracoes configuracoes = new Configuracoes();
    EditText eTCodigoEditar, eTCodigo1, eTCodigo2, eTCodigo3, eTCodigo4;
    EditText eTBR1, eTBR2, eTBR3, eTBR4, eTData1, eTData2, eTData3, eTData4, eTHora1, eTHora2, eTHora3, eTHora4;
    Button buttonSelecionar, buttonAvancar, buttonVoltar;

    //tela de Editar
    int indice = 1, indiceTotal = 4; //indice pra chegar na quantidade de linhas dos EditText
    int codigoEditar = 0; //pegar o ultimo codigo pra saber se chegou ao primeiro codigo das informacoes basicas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        abrirConexao();
        InicializaTelaEditar();
//        DesabilitaCampos();
    }
    //TODO clicar e o codigo do EditText ir para o campo de selecionar o codigo

    //criar a tela pra selecionar o codigo a ser editado
    public void InicializaTelaEditar () {
        setContentView(R.layout.activity_editar);
        buttonAvancar = (Button) findViewById(R.id.buttonAvancar);
        buttonVoltar = (Button) findViewById(R.id.buttonVoltar);
        buttonSelecionar = (Button) findViewById(R.id.buttonSelecionar);
        eTCodigoEditar = (EditText) findViewById(R.id.eTCodigoEditar);
        eTCodigo1 = (EditText) findViewById(R.id.eTCodigo1);
        eTCodigo2 = (EditText) findViewById(R.id.eTCodigo2);
        eTCodigo3 = (EditText) findViewById(R.id.eTCodigo3);
        eTCodigo4 = (EditText) findViewById(R.id.eTCodigo4);
        eTBR1 = (EditText) findViewById(R.id.eTBR1);
        eTBR2 = (EditText) findViewById(R.id.eTBR2);
        eTBR3 = (EditText) findViewById(R.id.eTBR3);
        eTBR4 = (EditText) findViewById(R.id.eTBR4);
        eTData1 = (EditText) findViewById(R.id.eTData1);
        eTData2 = (EditText) findViewById(R.id.eTData2);
        eTData3 = (EditText) findViewById(R.id.eTData3);
        eTData4 = (EditText) findViewById(R.id.eTData4);
        eTHora1 = (EditText) findViewById(R.id.eTHora1);
        eTHora2 = (EditText) findViewById(R.id.eTHora2);
        eTHora3 = (EditText) findViewById(R.id.eTHora3);
        eTHora4 = (EditText) findViewById(R.id.eTHora4);

//        PreencherTelaEditar();//preencher a tela editar com os dados
        codigoEditar = buscarUltimoCodigo(); //pega o ultimo codigo do BD
        indiceTotal = codigoEditar;
        buttonVoltar.setEnabled(false);
        PreencherTelaEditar2();
        eTCodigoEditar.setFocusable(true);

        buttonSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigo = Integer.valueOf(eTCodigoEditar.getText().toString()); //joga na variavel codigo
                contadorButtonEditar = true; //recebe true para dar update ao inves de insert.
                CarregaTelaInformacoesBasicas();
            }
        });
        buttonAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonVoltar.setEnabled(true);
                if (codigoEditar == Integer.valueOf(eTCodigo1.getText().toString())){
                    codigoEditar = codigoEditar - 4;
                }
                LimpaCampos();
                PreencherTelaEditar2();
            }
        });
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAvancar.setEnabled(true);

                if (codigoEditar+4 == Integer.valueOf(eTCodigo1.getText().toString())) {
                    codigoEditar = codigoEditar + 4;
                }
                LimpaCampos();
                PreencherTelaEditarBotaoVoltar();
            }
        });
        eTCodigo1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eTCodigoEditar.setText(eTCodigo1.getText().toString());
                return true;
            }
        });
        eTCodigo2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eTCodigoEditar.setText(eTCodigo2.getText().toString());
                return true;
            }
        });
        eTCodigo3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eTCodigoEditar.setText(eTCodigo3.getText().toString());
                return true;
            }
        });
        eTCodigo4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eTCodigoEditar.setText(eTCodigo4.getText().toString());
                return true;
            }
        });


    }//fim do metodo CarregaTelaEditar

    //carregar a tela das informacoes basicas
    public void CarregaTelaInformacoesBasicas () {
        Intent intentInformacoesBasicas = new Intent(EditarActivity.this, InformacoesBasicasActivity.class); //carregar a tela inicial de cadastro do acd
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", codigo);
        bundle.putBoolean("contadorButtonEditar", contadorButtonEditar);
        intentInformacoesBasicas.putExtras(bundle); //passar o codigo pra intent que sera acessado na proxima activity
        startActivity(intentInformacoesBasicas);
    }

    //Quando apertar o botao voltar, pra aumentar os codigos a serem mostrados na tabela
    private void PreencherTelaEditarBotaoVoltar() {
        if (codigoEditar < indiceTotal) {
            codigoEditar++;
            PreencheLinha4(codigoEditar);
            codigoEditar++;
            PreencheLinha3(codigoEditar);
            codigoEditar++;
            PreencheLinha2(codigoEditar);
            codigoEditar++;
            PreencheLinha1(codigoEditar);
            if (codigoEditar == indiceTotal) { //se for igual ao codigo total, desabilita o botao
                buttonVoltar.setEnabled(false);
            }
        } //fim do if

    }
    //versao 2 de preencher a tela editar
    private void PreencherTelaEditar2() {

            if (codigoEditar == 0) { // nao tem codigo nenhum
                mostrarMensagemBasica("Erro", "Sem arquivos.");
                buttonAvancar.setEnabled(false);
            } // fi do if para codigo 0
            else if (codigoEditar == 1) { //se tiver apenas um codigo
                PreencheLinha1(codigoEditar);
                buttonAvancar.setEnabled(false);
            } else if (codigoEditar == 2) { //se tiver apenas 2 codigos
                PreencheLinha1(codigoEditar);
                codigoEditar--;
                PreencheLinha2(codigoEditar);
                codigoEditar++; // vai receber mais um pra nse apertar o botao voltar, ele contabilizar normal os codigos e preencher a tela corretamente, e não partindo do codigoeditar == 1
                buttonAvancar.setEnabled(false);
            } else if (codigoEditar == 3) { // se tiver apenas 3 codigos
                PreencheLinha1(codigoEditar);
                codigoEditar--;
                PreencheLinha2(codigoEditar);
                codigoEditar--;
                PreencheLinha3(codigoEditar);
                codigoEditar = codigoEditar+2; //recebe dois pra tela ao voltar, ser preenchida corretamente.
                buttonAvancar.setEnabled(false);
            } else if (codigoEditar >= 4) { //se tiver 4 ou mais codigos.
                if (codigoEditar == 4) { //se for 4, nao precisa passar de tela
                    buttonAvancar.setEnabled(false);
                }
                PreencheLinha1(codigoEditar);
                codigoEditar--;
                PreencheLinha2(codigoEditar);
                codigoEditar--;
                PreencheLinha3(codigoEditar);
                codigoEditar--;
                PreencheLinha4(codigoEditar);
                codigoEditar--;
            } //fim do else if


    }//fim do preenchertela 2

    private void DesabilitaCampos() {
        eTBR1.setEnabled(false);
        eTBR2.setEnabled(false);
        eTBR3.setEnabled(false);
        eTBR4.setEnabled(false);
        eTCodigo1.setEnabled(false);
        eTCodigo2.setEnabled(false);
        eTCodigo3.setEnabled(false);
        eTCodigo4.setEnabled(false);
        eTData1.setEnabled(false);
        eTData2.setEnabled(false);
        eTData3.setEnabled(false);
        eTData4.setEnabled(false);
        eTHora1.setEnabled(false);
        eTHora2.setEnabled(false);
        eTHora3.setEnabled(false);
        eTHora4.setEnabled(false);
    }
    //limpar os campos durante preenchimento para nao haver dados incorretos.
    private void LimpaCampos () {
        eTBR1.setText("");
        eTBR2.setText("");
        eTBR3.setText("");
        eTBR4.setText("");
        eTCodigo1.setText("");
        eTCodigo2.setText("");
        eTCodigo3.setText("");
        eTCodigo4.setText("");
        eTData1.setText("");
        eTData2.setText("");
        eTData3.setText("");
        eTData4.setText("");
        eTHora1.setText("");
        eTHora2.setText("");
        eTHora3.setText("");
        eTHora4.setText("");
    }
    private void PreencheLinha1(int codigo) {
        String SQL = "Select * from " +configuracoes.getTabelaInformacoesBasicas() + " where codigo = " + codigo;
        try {
            abrirConexao();
            c = db.rawQuery(SQL, null);
            c.moveToFirst();
        }
        catch (Exception e) {

        }
        eTCodigo1.setText(c.getString(c.getColumnIndexOrThrow("codigo")));
        eTBR1.setText(c.getString(c.getColumnIndexOrThrow("br")));
        eTData1.setText(c.getString(c.getColumnIndexOrThrow("data")));
        eTHora1.setText(c.getString(c.getColumnIndexOrThrow("hora")));
    }//fim do PreencheLinha1

    private void PreencheLinha2(int codigo) {
        String SQL = "Select * from " +configuracoes.getTabelaInformacoesBasicas() + " where codigo = " + codigo;
        try {
            abrirConexao();
            c = db.rawQuery(SQL, null);
            c.moveToFirst();
        }
        catch (Exception e) {

        }
        eTCodigo2.setText(c.getString(c.getColumnIndexOrThrow("codigo")));
        eTBR2.setText(c.getString(c.getColumnIndexOrThrow("br")));
        eTData2.setText(c.getString(c.getColumnIndexOrThrow("data")));
        eTHora2.setText(c.getString(c.getColumnIndexOrThrow("hora")));
    }//fim do PreencheLinha2

    private void PreencheLinha3(int codigo) {
        String SQL = "Select * from " +configuracoes.getTabelaInformacoesBasicas() + " where codigo = " + codigo;
        try {
            abrirConexao();
            c = db.rawQuery(SQL, null);
            c.moveToFirst();
        }
        catch (Exception e) {

        }
        eTCodigo3.setText(c.getString(c.getColumnIndexOrThrow("codigo")));
        eTBR3.setText(c.getString(c.getColumnIndexOrThrow("br")));
        eTData3.setText(c.getString(c.getColumnIndexOrThrow("data")));
        eTHora3.setText(c.getString(c.getColumnIndexOrThrow("hora")));
    }//fim do PreencheLinha3

    private void PreencheLinha4(int codigo) {
        String SQL = "Select * from " +configuracoes.getTabelaInformacoesBasicas() + " where codigo = " + codigo;
        try {
            abrirConexao();
            c = db.rawQuery(SQL, null);
            c.moveToFirst();
        }
        catch (Exception e) {

        }
        eTCodigo4.setText(c.getString(c.getColumnIndexOrThrow("codigo")));
        eTBR4.setText(c.getString(c.getColumnIndexOrThrow("br")));
        eTData4.setText(c.getString(c.getColumnIndexOrThrow("data")));
        eTHora4.setText(c.getString(c.getColumnIndexOrThrow("hora")));
    }//fim do PreencheLinha4

    //mostrar a mensagem basica de confirmação de algo (erro, sucesso, etc).
    private void mostrarMensagemBasica(String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(title).setMessage(mensagem).setNeutralButton("OK", null).show();
    }//fim do metodo mostrarMensagemBasica

    public SQLiteDatabase abrirConexao () { //Abre a conexao com o BD
        try {
            db = openOrCreateDatabase(configuracoes.getNomeBanco(), Context.MODE_PRIVATE, null);
        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro!", "Erro ao abrir conexão com o BD: " +e.toString());
        }//fim do catch para abrir conexao
        return db;
    }//fim do metodo abrirConexao

    private int buscarUltimoCodigo() {
        try { //buscar o ultimo codigo do acd.
            abrirConexao();
            c = db.query(configuracoes.getTabelaInformacoesBasicas(), new String[]{"codigo"}, null, null, null, null, null);
            c.moveToLast();
            codigo = c.getInt(c.getColumnIndexOrThrow("codigo"));

        } catch (Exception e) {
            codigo = 0;
        }// fim do catch
        return codigo; //retornar o ultimo codigo
    } //fim do buscarUltimoCodigo

    private void LimparTelas () {
        Intent intent = new Intent(getApplicationContext(), BRBrasilisActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() { //fechar todas as telas, e abrir a primeira
        LimparTelas();
    }

}
