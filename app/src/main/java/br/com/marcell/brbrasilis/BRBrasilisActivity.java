package br.com.marcell.brbrasilis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BRBrasilisActivity extends Activity {

    boolean contadorButtonEditar = false;
    int codigo = 0; //recebe o codigo (novo ou abrir) para manipulação dos dados

    Button buttonNovo; //criar um novo registro de acd
    Button buttonEditar; //editar um registro já criado
    Button buttonInformacoes;
    Button buttonSair; //sair do sistema

    Configuracoes configuracoes = new Configuracoes(); //instanciar para buscar os metodos da outra classe

    SQLiteDatabase db;
    Cursor c;

    int quantidadeDePassageiros; //vai receber a quantidade de passagerios de cada veiculo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        criarBancoDados();
        CarregaTelaBRBrasilis();

        // Habilita novos recursos na janela dizendo que terá recursos e será um icone a esquerda
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        // Adiciona um ícone a esquerda
//        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
//                R.drawable.droid_icon);
    }
    //Carrega tela inicial.
    public void CarregaTelaBRBrasilis () {
        setContentView(R.layout.activity_brbrasilis);
        buttonNovo = (Button) findViewById(R.id.buttonNovo);
        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonInformacoes = (Button) findViewById(R.id.buttonInformacoes);
        buttonSair = (Button) findViewById(R.id.buttonSair);

        //clicar no botao novo na tela inicial
        buttonNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = abrirConexao();
                try { //buscar o ultimo codigo do acd.
                    c = db.query(configuracoes.getTabelaInformacoesBasicas(), new String[]{"codigo"}, null, null, null, null, null);
                    c.moveToLast();
                    codigo = c.getInt(c.getColumnIndexOrThrow("codigo"));

                } catch (Exception e) {
//                    mostrarMensagemBasica("Erro!", "Erro ao manipular código: " + e.toString());
                }
                if (codigo >= 1) { // se tiver pega o ultimo codigo e add mais um.
                    codigo++;
                } else { // se nao tiver nnenhum codigo codigo será o primeiro
                    codigo = 1;
                }

                try {
                    //buscar o ultimo codigo dos passageiros
                    c = db.query(configuracoes.getTabelaPassageiros(), new String[]{"codigopassageiros"}, null, null, null, null, null);
                    c.moveToLast();
                    Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] = c.getInt(c.getColumnIndexOrThrow("codigopassageiros"));
                } catch (Exception e) {
//                    mostrarMensagemBasica("Erro!", "Erro ao manipular código passageiros: " + e.toString());
                }
                if (Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] >= 1) {
                    Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros]++;
                } else {
                    Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] = 1;
                }

                inicializarVariaveis();
                CarregaTelaInformacoesBasicas();
            }
        });

        //clicar no botao editar na tela inicial.
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuracoes.iteracaoPassageiros = 1; // voltar para um
                Configuracoes.iteracaoVeiculos = 1;
                abrirConexao();
                CarregaTelaEditar();

            }
        });//fim do buttonEditar Click

        buttonInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSobre = new Intent(BRBrasilisActivity.this, SobreActivity.class);
                startActivity(intentSobre);
            }
        });
        buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }//fim do carregaTelaBrBrasilis

    //carregar a tela das informacoes basicas
    public void CarregaTelaInformacoesBasicas () {
        Intent intentInformacoesBasicas = new Intent(BRBrasilisActivity.this, InformacoesBasicasActivity.class); //carregar a tela inicial de cadastro do acd
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", codigo);
        bundle.putBoolean("contadorButtonEditar", contadorButtonEditar);
        intentInformacoesBasicas.putExtras(bundle); //passar o codigo pra intent que sera acessado na proxima activity
        startActivity(intentInformacoesBasicas);
    }

    private void teste() {

    }
    public void CarregaTelaEditar() {
        Intent intentEditar = new Intent(BRBrasilisActivity.this, EditarActivity.class);
        startActivity(intentEditar);
    }
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brbrasilis, menu);
        return true;
    }

    //metodo para inicializar variaveis após clicar em novo, editar
    private void inicializarVariaveis () {
        Configuracoes.iteracaoPassageiros = 1;
        Configuracoes.iteracaoVeiculos = 1;
        quantidadeDePassageiros = 0;
        contadorButtonEditar = false;

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

    //Metodo para verificar e criar banco de dados.
    public void criarBancoDados () {
        db = abrirConexao();
        try {
            //criar a tabela das informações básicas.
            db.execSQL("create table if not exists " + configuracoes.getTabelaInformacoesBasicas() + " (codigo int not null primary key, " +
                    "br varchar(3) not null, km varchar(6) not null, sentido varchar(2) not null, data varchar(11) not null, hora varchar(6) not null, " +
                    " quantidadeDeVeiculos varchar(2) not null)");
            //tabela de veiculos
            db.execSQL("create table if not exists " + configuracoes.getTabelaVeiculos() + " (codigo int not null, v varchar(2) not null, " +
            "placa varchar(7) not null, quantidadeDePassageiros varchar(2) not null)");
            //tabela de condutor
            db.execSQL("create table if not exists " + configuracoes.getTabelaCondutor() + " (codigo int not null, " +
            "placacondutor varchar (7), v varchar(2), nome varchar(70), CPF varchar(15) not null, naturalidade varchar(45), "+
            "estadocivil varchar(45), telefone varchar(45), escolaridade varchar(45), endereco varchar(70), ocupacao varchar(45), "+
            "cinto varchar(2), etilometro varchar(45), PRIMARY KEY (CPF))");
            //tabela de passageiros
            db.execSQL("create table if not exists " + configuracoes.getTabelaPassageiros() + "(codigo int not null, codigopassageiros int not null, " +
            "placapassageiros varchar(7), v varchar(2), nome varchar(70), documento varchar(30), naturalidade varchar(45), "+
            "estadocivil varchar(45), telefone varchar(45), escolaridade varchar(45), endereco varchar(70), ocupacao varchar(45),"+
            "cinto varchar(2), PRIMARY KEY (codigopassageiros))");

        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro!", "Erro ao criar tabelas: " + e.toString());
        }

    }

    //mostrar a mensagem basica de confirmação de algo (erro, sucesso, etc).
    private void mostrarMensagemBasica(String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(title).setMessage(mensagem).setNeutralButton("OK", null).show();
    }//fim do metodo mostrarMensagemBasica

    @Override
    public void onBackPressed() { //se pressionar o botão voltar.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair?");
        builder.setMessage("Deseja realmente sair?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.menu_sobre:
                Intent intentSobre = new Intent(BRBrasilisActivity.this, SobreActivity.class);
                startActivity(intentSobre);

                break;
//            case android.R.id.home:
//
//                break;
//            case android.R.id.closeButton:
//
//                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
