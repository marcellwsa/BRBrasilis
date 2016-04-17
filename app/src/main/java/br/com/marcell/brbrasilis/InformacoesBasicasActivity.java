package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InformacoesBasicasActivity extends Activity {

    int codigo;
    Boolean contadorButtonEditar= false;

    SQLiteDatabase db;
    Cursor c;
    //tela de informações basicas

    EditText eTBR, eTData, eTHora, eTKM; //BR, data e hora do acontecimento, KM
    RadioGroup radioGroupSentido; //agrupa os sentidos, C ou D
    RadioButton radioButtonCrescente, radioButtonDecrescente; //Define se é C ou D
    Button buttonVeiculos; //passa pra tela de cadastro de veiculos, apos definir os dados basicos
    TextView textViewBR, textViewKM;
    Spinner spinnerQuantidadeVeiculos; //selecionar a quantidad ede veiculos, até no maximo 7
    int quantidadeDeVeiculos; //recebe a quantidade de veiculos envolvidos no acd atraves da selecao no spinner

    Configuracoes configuracoes = new Configuracoes();
    InformacoesBasicas informacoesBasicas = new InformacoesBasicas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_basicas);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        receberVariaveis();
        InicializaTelaInformacoesBasicas();
    }

    //pega as variaveis referentes as outras activitys.
    private void receberVariaveis() {
        try {
            Intent i = getIntent();
            codigo = i.getIntExtra("codigo", 1); //recebe o codigo que foi digitado na outra activity e o 1 seria o padrao caso nao tivesse sido definico o codigo
            contadorButtonEditar = i.getBooleanExtra("contadorButtonEditar", false);

        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro", "Erro ao inicializar código.");
        }
    } //fim do receberVariaveis

    //Carrega tela das informações básicas.
    public void InicializaTelaInformacoesBasicas(){
        setContentView(R.layout.activity_informacoes_basicas);
        eTBR = (EditText) findViewById(R.id.eTBR);
        radioButtonCrescente = (RadioButton) findViewById(R.id.radioButtonCrescente);
        radioButtonCrescente.setChecked(true); //já deixar selecionado
        radioButtonDecrescente = (RadioButton) findViewById(R.id.radioButtonDecrescente);
        eTData = (EditText) findViewById(R.id.eTData);
        eTHora = (EditText) findViewById(R.id.eTHora);
        eTKM = (EditText) findViewById(R.id.eTKM);
        textViewBR = (TextView) findViewById(R.id.textViewBR); //textview
        textViewKM = (TextView) findViewById(R.id.textViewKM);
        buttonVeiculos = (Button) findViewById(R.id.buttonVeiculos);
        spinnerQuantidadeVeiculos = (Spinner) findViewById(R.id.spinnerQuantidadeVeiculos);

        //colocar a data atual primeira versao
//        eTData.setText(DateFormat.getDateInstance().format(new Date()));

        abrirConexao();

        ArrayAdapter array = ArrayAdapter.createFromResource(this, R.array.opcoesQuantidadeVeiculos, android.R.layout.simple_spinner_item);
        spinnerQuantidadeVeiculos.setAdapter(array);

        if (contadorButtonEditar) { // se nao for editar, ele vai inserir a data e hora atual.

//            codigo = Integer.valueOf(eTCodigoEditar.getText().toString());
            spinnerQuantidadeVeiculos.setEnabled(false); //nao alterar qt de veiculos envolvidos
            String SQL = "Select * from " + configuracoes.getTabelaInformacoesBasicas() + " where codigo = " + codigo;
            try {
                c = db.rawQuery(SQL, null);
                c.moveToFirst();
                eTData.setText(c.getString(c.getColumnIndexOrThrow("data")));
                eTBR.setText(c.getString(c.getColumnIndexOrThrow("br")));
                eTHora.setText(c.getString(c.getColumnIndexOrThrow("hora")));
                eTKM.setText(c.getString(c.getColumnIndexOrThrow("km")));
                if (c.getString(c.getColumnIndexOrThrow("sentido")).equals("C")) {
                    radioButtonCrescente.setChecked(true);
                } else {
                    radioButtonDecrescente.setChecked(true);
                }
                spinnerQuantidadeVeiculos.setSelection(c.getInt(c.getColumnIndexOrThrow("quantidadeDeVeiculos")));

            } catch (SQLException e) { //TODO se nao tiver nenhum registro para alterar, mostrar o erro.
                mostrarMensagemBasica("Erro!", "Erro ao editar as informações básicas: " + e.toString());
            }//fim do catch

        } else {

            configuracoes.pegarDataAtual(eTData);
            configuracoes.pegarHoraAtual(eTHora);
            spinnerQuantidadeVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    quantidadeDeVeiculos = spinnerQuantidadeVeiculos.getSelectedItemPosition(); //pega a posição selecionada no spinner
                    if (contadorButtonEditar) { //se for editar nao vai aparecer a mensagem

                    } else {
                        if (quantidadeDeVeiculos == 0) {
                            //nao faz nada, pois nada foi selecionado ainda.
                        } else {
                            mostrarMensagemBasica("OK", "Confirma " + quantidadeDeVeiculos + " veículo(s) envolvidos?");
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
        //clicar no botao veiculos para salvar, e abrir a proxima tela de cadastrar os veiculos.
        buttonVeiculos.setOnClickListener(new View.OnClickListener() { //aciona a tela de veiculos
            @Override
            public void onClick(View view) {
                informacoesBasicas.setBr(eTBR.getText().toString());
                informacoesBasicas.setKm(eTKM.getText().toString());

                if (radioButtonDecrescente.isChecked()) {
                    informacoesBasicas.setSentido("D");
                } else if (radioButtonCrescente.isChecked()) {
                    informacoesBasicas.setSentido("C");
                }
                informacoesBasicas.setQuantidadeDeVeiculos(String.valueOf(spinnerQuantidadeVeiculos.getSelectedItemPosition()));
                quantidadeDeVeiculos = Integer.valueOf(informacoesBasicas.getQuantidadeDeVeiculos());
                informacoesBasicas.setData((eTData.getText().toString()));
                informacoesBasicas.setHora(eTHora.getText().toString());

                if (informacoesBasicas.getBr().isEmpty()) { //se BR nao conter nada dentro
                    mostrarMensagemBasica("Erro!", "Campo BR é obrigatório! ");
                    textViewBR.setTextColor(Color.RED); //colocar vermelho para saber que faltou incluir esse item.

                } else if (informacoesBasicas.getKm().isEmpty()) {
                    mostrarMensagemBasica("Erro!", "Campo KM é obrigatório! ");
                    textViewKM.setTextColor(Color.RED); //colocar vermelho para saber que faltou incluir esse item.

                } else if (informacoesBasicas.getQuantidadeDeVeiculos().isEmpty() || informacoesBasicas.getQuantidadeDeVeiculos().equals("0")) {
                    mostrarMensagemBasica("Erro!", "Selecione a quantidade de veículos! ");

                } else {
                    ContentValues valores = new ContentValues();
                    valores.put("codigo", codigo);
                    valores.put("br", informacoesBasicas.getBr());
                    valores.put("km", informacoesBasicas.getKm());
                    valores.put("sentido", informacoesBasicas.getSentido());
                    valores.put("data", informacoesBasicas.getData());
                    valores.put("hora", informacoesBasicas.getHora());
                    valores.put("quantidadeDeVeiculos", informacoesBasicas.getQuantidadeDeVeiculos());

                    if (contadorButtonEditar) { //TODO aumentar numero de veiculos
                        try {
                            db.update(configuracoes.getTabelaInformacoesBasicas(), valores, "codigo=" + codigo, null);
//                            mostrarMensagemBasicaSucesso("OK!", "Código: " + codigo + " atualizado com sucesso.");
                            Toast.makeText(InformacoesBasicasActivity.this, "Código: " + codigo + " atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                            CarregaTelaVeiculos(); //carregar para preencher os dados

                        } //fim do try para editar
                        catch (Exception e) {
                            mostrarMensagemBasica("Erro!", "Erro ao atualizar as informações básicas: " + e.toString());
                        }//fim do catch para editar
                    } else {
                        try {
                            db.insert(configuracoes.getTabelaInformacoesBasicas(), null, valores); //inserir valores
//                            mostrarMensagemBasicaSucesso("OK", "Código: " + codigo + " inserido com sucesso... "); //abre a tela de veiculos
                            textViewBR.setTextColor(Color.BLACK); //voltar a cor original.
                            textViewKM.setTextColor(Color.BLACK);
                            Toast.makeText(InformacoesBasicasActivity.this, "Código: " + codigo + " inserido com sucesso... ", Toast.LENGTH_SHORT).show();
                            CarregaTelaVeiculos();
                        } //fim do try para salvar
                        catch (Exception e) {
                            mostrarMensagemBasica("Erro!", "Erro ao salvar as informações básicas: " + e.toString());
                        } //fim do catch para salvar
                    }

                } //fim do else após checar dados

            }
        });

    }//fim do carregaTelaInformacoesBasicas


    //carregar a tela de veiculos
    public void CarregaTelaVeiculos() {
        Intent intentVeiculos = new Intent(InformacoesBasicasActivity.this, VeiculosActivity.class); //carregar a tela de veiculos  do acd
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", codigo);
        bundle.putBoolean("contadorButtonEditar", contadorButtonEditar);
        bundle.putInt("quantidadeDeVeiculos", quantidadeDeVeiculos);
        intentVeiculos.putExtras(bundle); //passar o codigo pra intent que sera acessado na proxima activity
        startActivity(intentVeiculos);
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

    //mostrar a mensagem basica de confirmação de sucesso fecha a tela, e abre a proxima tela.
    private void mostrarMensagemBasicaSucesso(String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                CarregaTelaVeiculos();
            }
        });
        alerta.setTitle(title).setMessage(mensagem).show();
    }//fim do metodo mostrarMensagemBasicaSucesso
}
