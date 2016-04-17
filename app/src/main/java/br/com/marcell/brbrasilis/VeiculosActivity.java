package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class VeiculosActivity extends Activity {

    int codigo;
    Boolean contadorButtonEditar= false;

    SQLiteDatabase db;
    Cursor c;

    String orientacao; //receber a string da orientacao quanto a foto que for tirada
    //tela de veiculos
    EditText eTPlaca, eTV, eTTotalPassageiros; //receber a placa, e automaticamente o numero do veiculo
    int quantidadeDeVeiculos; //recebe a quantidade de veiculos envolvidos no acd atraves da selecao no spinner
    int quantidadeDePassageiros; //vai receber a quantidade de passagerios de cada veiculo
    Button buttonCondutor, buttonFotoFrente, buttonFotoTraseira, buttonFotoDireita, buttonFotoEsquerda; //passa para tela de cadastro de condutores;
    Button buttonAbrirFotos;
    ImageView imageViewFrente, imageViewdireita, imageViewEsquerda, imageViewTras;
    Configuracoes configuracoes = new Configuracoes();
    Veiculos veiculos = new Veiculos();
    InformacoesBasicas informacoesBasicas = new InformacoesBasicas();

    TextView textViewLocalFotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculos);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        ReceberVariaveis();
        InicializaTelaVeiculos();
        abrirConexao();
    }

    //pega as variaveis referentes as outras activitys. Codigo do acd e buttoneditar e quantidade de veiculos
    private void ReceberVariaveis() {
        try {
            Intent i = getIntent();
            codigo = i.getIntExtra("codigo", 1); //recebe o codigo que foi digitado na outra activity e o 1 seria o padrao caso nao tivesse sido definico o codigo
            quantidadeDeVeiculos = i.getIntExtra("quantidadeDeVeiculos", 1);
            contadorButtonEditar = i.getBooleanExtra("contadorButtonEditar", false);

        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro", "Erro ao inicializar variáveis de veículos." + e.toString());
        }
    } //fim do receberVariaveis

    public void InicializaTelaVeiculos () {
        setContentView(R.layout.activity_veiculos);
        eTPlaca = (EditText) findViewById(R.id.eTPlaca);
        eTV = (EditText) findViewById(R.id.eTV);
        eTV.setEnabled(false);//sempre falso para edição.
        eTTotalPassageiros = (EditText) findViewById(R.id.eTTotalPassageiros);
        buttonCondutor = (Button) findViewById(R.id.buttonCondutor);
//        imageViewdireita = (ImageView) findViewById(R.id.imageViewDireita);
//        imageViewEsquerda = (ImageView) findViewById(R.id.imageViewEsquerda);
//        imageViewFrente = (ImageView) findViewById(R.id.imageViewFrente);
//        imageViewTras = (ImageView) findViewById(R.id.imageViewTras);
        buttonFotoFrente = (Button) findViewById(R.id.buttonFotoFrente);
        buttonFotoDireita = (Button) findViewById(R.id.buttonFotoDireita);
        buttonFotoEsquerda = (Button) findViewById(R.id.buttonFotoEsquerda);
        buttonFotoTraseira = (Button) findViewById(R.id.buttonFotoTraseira);
        buttonAbrirFotos = (Button) findViewById(R.id.buttonAbrirFotos);
        textViewLocalFotos = (TextView)findViewById(R.id.textViewLocalFotos);
        textViewLocalFotos.setVisibility(View.INVISIBLE); //deixar invisivel

        eTV.setText(Integer.toString(Configuracoes.iteracaoVeiculos)); //String.valueOf();

        if (contadorButtonEditar) { //preencher com as informacoes dos veiculos
            eTTotalPassageiros.setEnabled(false); //desativar pra nao alterar a qt de passageiros. //TODO permitir que se altere qt de passag

            textViewLocalFotos.setVisibility(View.VISIBLE); //deixa visivel
            textViewLocalFotos.setText("Local das fotos: " + "/BRBrasilis/Fotos/" + codigo + "/");

            String SQL = "Select * from " +configuracoes.getTabelaVeiculos() + " where codigo = " + codigo + " and v = " + Configuracoes.iteracaoVeiculos;
            try {
                abrirConexao();
                c = db.rawQuery(SQL, null);
                c.moveToFirst();
                eTV.setText(c.getString(c.getColumnIndexOrThrow("v")));
                eTPlaca.setText((c.getString(c.getColumnIndexOrThrow("placa"))));
                eTTotalPassageiros.setText((c.getString(c.getColumnIndexOrThrow("quantidadeDePassageiros"))));
            }catch (Exception e) {

            }//fim do catch


        } //fim do if para setar os campos selecionados com o editar

        buttonCondutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eTTotalPassageiros.getText().toString().isEmpty()) {
                    veiculos.setQuantidadeDePassageiros(0);
                    veiculos.setQuantidadeDePassageirosSTR("0");
                } else {
                    veiculos.setQuantidadeDePassageiros(Integer.valueOf(eTTotalPassageiros.getText().toString())); //pegar a quantidade de passageiros.
                    quantidadeDePassageiros = veiculos.getQuantidadeDePassageiros();
                    veiculos.setQuantidadeDePassageirosSTR(eTTotalPassageiros.getText().toString()); //string para o BD
                }
                //pegar os dados dos eTs para salvar ou atualizar
                veiculos.setPlaca(eTPlaca.getText().toString());
                veiculos.setV(eTV.getText().toString()); //pegar o v

                if (veiculos.getPlaca().isEmpty()) { //se placa estiver vazio
                    mostrarMensagemBasica("Erro!", "Campo PLACA é obrigatório! ");
                }
//                else if (checarPlaca(eTPlaca.getText().toString()) && !contadorButtonEditar) {
//                    mostrarMensagemBasica("Erro", "Placa já existe no cadastro.");
//
//                }
                else {
                    ContentValues valoresVeiculos = new ContentValues();
                    valoresVeiculos.put("codigo", codigo);
                    valoresVeiculos.put("v", veiculos.getV());
                    valoresVeiculos.put("placa", veiculos.getPlaca());
                    valoresVeiculos.put("quantidadeDePassageiros", veiculos.getQuantidadeDePassageirosSTR());
                    if (contadorButtonEditar) { //fazer o update
                        try {
                            db.update(configuracoes.getTabelaVeiculos(), valoresVeiculos, "codigo = " + codigo + " and v = " + Configuracoes.iteracaoVeiculos, null);
//                            mostrarMensagemBasicaSucesso("OK!", "V" + Configuracoes.iteracaoVeiculos + " atualizado com sucesso... ");
                            Toast.makeText(VeiculosActivity.this, "V" + Configuracoes.iteracaoVeiculos + " atualizado com sucesso... ", Toast.LENGTH_SHORT).show();
                            CarregaTelaCondutor();
                        } catch (Exception e) {
                            mostrarMensagemBasica("Erro!", "Erro ao atualizar V" + Configuracoes.iteracaoVeiculos + "  " + e.toString());
                        }

                    } else { //else para inserir os dados
                        try {
                            db.insert(configuracoes.getTabelaVeiculos(), null, valoresVeiculos); //inserir valores
//                            mostrarMensagemBasicaSucesso("OK", "V" + Configuracoes.iteracaoVeiculos + " inserido com sucesso... ");
                            Toast.makeText(VeiculosActivity.this, "V" + Configuracoes.iteracaoVeiculos + " inserido com sucesso... ", Toast.LENGTH_SHORT).show();
                            CarregaTelaCondutor();
                        } catch (Exception e) {
                            mostrarMensagemBasica("Erro!", "Erro ao salvar veículos: " + e.toString());
                        }
                    }
                }
            }
        });

        buttonFotoFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eTPlaca.getText().toString().equals("")){
                    mostrarMensagemBasica("Erro", "Digite a placa do veículo!");
                }
                else {
                    tirarFoto("frente");
                    textViewLocalFotos.setVisibility(View.VISIBLE); //deixa visivel
                    textViewLocalFotos.setText("Local das fotos: "+ "/BRBrasilis/Fotos/"+codigo+"/");
                }

            }
        });

        buttonFotoEsquerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eTPlaca.getText().toString().equals("")){
                    mostrarMensagemBasica("Erro", "Digite a placa do veículo!");
                }
                else {
                    tirarFoto("esquerda");
                }


            }
        });

        buttonFotoDireita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eTPlaca.getText().toString().equals("")){
                    mostrarMensagemBasica("Erro", "Digite a placa do veículo!");
                }
                else {
                    tirarFoto("direita");
                }
                }
        });
        buttonFotoTraseira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eTPlaca.getText().toString().equals("")){
                    mostrarMensagemBasica("Erro", "Digite a placa do veículo!");
                }
                else{
                    tirarFoto("traseira");
                }

            }
        });

        buttonAbrirFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eTPlaca.getText().toString() == null || eTPlaca.getText().toString().equals("")) { //se for nulo, não abre a pagina de vizualizar fotos
                    mostrarMensagemBasica("Erro", "Placa inválida.");
                }
                else {
                    Intent intentFotos = new Intent(VeiculosActivity.this, FotosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("codigo", codigo);
                    bundle.putString("placa", eTPlaca.getText().toString());
                    intentFotos.putExtras(bundle);
                    startActivity(intentFotos); //abre as fotos, voltando pra essa tela, após fechar
                }

//                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/sdcard/BRBrasilis/Fotos/");
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(selectedUri, "resource/folder");
//                startActivity(intent);
            }
        });
    }//fim do metodo CarregaTelaVeiculos

    //carregar a tela de Condutores
    public void CarregaTelaCondutor() {
        Intent intentCondutor = new Intent(VeiculosActivity.this, CondutorActivity.class); //carregar a tela de condutores  do acd
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", codigo);
        bundle.putBoolean("contadorButtonEditar", contadorButtonEditar);
        bundle.putInt("quantidadeDeVeiculos", quantidadeDeVeiculos);
        bundle.putInt("quantidadeDePassageiros", quantidadeDePassageiros);
        bundle.putString("placa", veiculos.getPlaca());
        intentCondutor.putExtras(bundle); //passar o codigo pra intent que sera acessado na proxima activity
        startActivity(intentCondutor);
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

    //mostrar a mensagem basica de confirmação de algo (erro, sucesso, etc).
    private void mostrarMensagemBasicaSucesso(String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                CarregaTelaCondutor();
            }
        });
        alerta.setTitle(title).setMessage(mensagem).show();
    }//fim do metodo mostrarMensagemBasica

    //Tirar fotos
    public void tirarFoto(String orientacao){
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/BRBrasilis/Fotos/"+codigo+"/"+eTPlaca.getText().toString()+"-"+orientacao+".jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivity(intent);
    }

    private void LimparTelas () {
        Intent intent = new Intent(getApplicationContext(), BRBrasilisActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }//fim do limpa telas

    @Override
    public void onBackPressed() { //fechar todas as telas, e abrir a primeira
        if (!contadorButtonEditar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sair?");
            builder.setMessage("Se voltar, todo o seu progresso será perdido. Deseja?");
            builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    LimparTelas();
                }
            });
            builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog alerta = builder.create();
            alerta.show();
        }else {
            finish();
        }


    }

//    private boolean checarPlaca (String placa) {
//        String placaBD = ""; //placa quem busca o banco
//        String SQL = "Select * from " +configuracoes.getTabelaVeiculos() + " where placa = " + placa;
//        try {
//            abrirConexao();
//            Cursor c2 = db.rawQuery(SQL, null);
//            c2.moveToFirst();
//            placaBD = c2.getString(c2.getColumnIndexOrThrow("placa"));
//            mostrarMensagemBasica("Placa", placaBD +" e placa parametro é: "+ placa );
//        }//fim do try
//        catch (Exception e) {
//          mostrarMensagemBasica("Catch", "Catch");
//        }//fim do catch
//        if (placaBD.equals(placa)) {
//            return true;
//        }
//        else {
//            return false;
//        }
//    } //fim do checar placa

    }
