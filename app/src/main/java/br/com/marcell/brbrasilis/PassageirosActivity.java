package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PassageirosActivity extends Activity {

    int codigo;
    Boolean contadorButtonEditar= false;
    int erro; //definir algumas acoes
    SQLiteDatabase db;
    Cursor c;
    Cursor cPassageiros;

    Configuracoes configuracoes = new Configuracoes();
    Passageiros passageiros = new Passageiros();

    int quantidadeDeVeiculos; //recebe a quantidade de veiculos envolvidos no acd atraves da selecao no spinner
    int quantidadeDePassageiros; //vai receber a quantidade de passagerios de cada veiculo

    //tela de passageiros
    EditText eTPassageiroV;
    Button buttonProsseguir;
    EditText eTNomePassageiro, eTDocumentoPassageiro, eTNaturalidadePassageiro, eTTelefonePassageiro;
    EditText eTEnderecoPassageiro, eTOcupacaoPassageiro;
    CheckBox checkBoxCintoPassageiro;
    AutoCompleteTextView eTEstadoCivilPassageiro, eTEscolaridadePassageiro;
    TextView textViewPassageiroDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiros);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        ReceberVariaveis();
        InicializaTelaPassageiros();
        abrirConexao();
    }

    //pega as variaveis referentes as outras activitys. Codigo do acd e buttoneditar e quantidade de veiculos
    private void ReceberVariaveis() {
        try {
            Intent i = getIntent();
            codigo = i.getIntExtra("codigo", 1); //recebe o codigo que foi digitado na outra activity e o 1 seria o padrao caso nao tivesse sido definico o codigo
            contadorButtonEditar = i.getBooleanExtra("contadorButtonEditar", false);
            quantidadeDeVeiculos = i.getIntExtra("quantidadeDeVeiculos", 1);
            quantidadeDePassageiros = i.getIntExtra("quantidadeDePassageiros", 0);
            passageiros.setPlacaPassageiros(i.getStringExtra("placa"));
        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro", "Erro ao inicializar variáveis de Condutor." + e.toString());
        }
    } //fim do receberVariaveis

    //carrega a tela para inclusao dos passageiros
    public void InicializaTelaPassageiros() {
        setContentView(R.layout.activity_passageiros);
        eTPassageiroV = (EditText) findViewById(R.id.eTPassageiroV);
        buttonProsseguir = (Button) findViewById(R.id.buttonProsseguir);
        eTNomePassageiro = (EditText) findViewById(R.id.eTNomePassageiro);
        eTDocumentoPassageiro = (EditText) findViewById(R.id.eTDocumentoPassageiro);
        eTNaturalidadePassageiro = (EditText) findViewById(R.id.eTNaturalidadePassageiro);
        eTEstadoCivilPassageiro = (AutoCompleteTextView) findViewById(R.id.eTEstadoCivilPassageiro);
        eTTelefonePassageiro = (EditText) findViewById(R.id.eTTelefonePassageiro);
        eTEscolaridadePassageiro = (AutoCompleteTextView) findViewById(R.id.eTEscolaridadePassageiro);
        eTEnderecoPassageiro = (EditText) findViewById(R.id.eTEnderecoPassageiro);
        eTOcupacaoPassageiro = (EditText) findViewById(R.id.eTOcupacaoPassageiro);
        checkBoxCintoPassageiro = (CheckBox) findViewById(R.id.checkBoxCintoPassageiro);

        ArrayAdapter arrayEstadoCivil = ArrayAdapter.createFromResource(this, R.array.opcoesEstadoCivil, android.R.layout.select_dialog_item);
        eTEstadoCivilPassageiro.setThreshold(1);
        eTEstadoCivilPassageiro.setAdapter(arrayEstadoCivil);

        ArrayAdapter arrayEscolaridade = ArrayAdapter.createFromResource(this, R.array.opcoesEscolaridade, android.R.layout.select_dialog_item);
        eTEscolaridadePassageiro.setThreshold(1); //quantidade de caracteres para digitar até aparecer as opcoes
        eTEscolaridadePassageiro.setAdapter(arrayEscolaridade);

        eTPassageiroV.setEnabled(false);//sempre falso para edição.
        eTPassageiroV.setText(Integer.toString(Configuracoes.iteracaoVeiculos));

        if (contadorButtonEditar){

            if(Configuracoes.iteracaoPassageiros == 2){ //se for o primeiro ou só tiver um passageiro, entra aqui
                String SQL = "Select * from " +configuracoes.getTabelaPassageiros() + " where codigo = " + codigo + " and v = " + Configuracoes.iteracaoVeiculos;
                try {
                    abrirConexao();
                    c = db.rawQuery(SQL, null);
                    c.moveToFirst();
                    //popular os codigos dos passageiros com os obtidos do bd
                    for (Configuracoes.contadorCodigoPassageiros = 0; Configuracoes.contadorCodigoPassageiros < c.getCount(); Configuracoes.contadorCodigoPassageiros++) {
                        Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] = c.getInt(c.getColumnIndexOrThrow("codigopassageiros"));
                        c.moveToNext();
                    } //fim do laço for
                        Configuracoes.contadorCodigoPassageiros = 0; //zerar o contador
                }
                catch (Exception e) {

                }//fim do catch
                //pegar os dados referentes ao primeiro codigo encontrado no BD dos passageiros e colocar no cursor
                String SQL2 = "Select * from " +configuracoes.getTabelaPassageiros() + " where codigopassageiros = " + Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros];
                try {
                    abrirConexao();
                    cPassageiros = db.rawQuery(SQL2, null);
                    cPassageiros.moveToFirst();
                }
                catch (Exception e) {

                }//fim do catch

            }else { //se tiver mais de um passageiro vai iterar aqui pegando os codigos restantes
                String SQL2 = "Select * from " +configuracoes.getTabelaPassageiros() + " where codigopassageiros = " + Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros];
                try {
                    abrirConexao();
                    cPassageiros = db.rawQuery(SQL2, null);
                    cPassageiros.moveToFirst();
                }
                catch (Exception e) {

                }
            }//fim do else
            //popular os dados
            Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] = cPassageiros.getInt(cPassageiros.getColumnIndexOrThrow("codigopassageiros")); //
            eTPassageiroV.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("v")));
            eTNomePassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("nome")));
            eTDocumentoPassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("documento")));
            eTNaturalidadePassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("naturalidade")));
            eTEstadoCivilPassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("estadocivil")));
            eTTelefonePassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("telefone")));
            eTEscolaridadePassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("escolaridade")));
            eTEnderecoPassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("endereco")));
            eTOcupacaoPassageiro.setText(cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("ocupacao")));
            if (cPassageiros.getString(cPassageiros.getColumnIndexOrThrow("cinto")).equals("S")) {
                checkBoxCintoPassageiro.setChecked(true);
            }else {
                checkBoxCintoPassageiro.setChecked(false);
            }
            //Mostrar em qual passageiro está de um total de X passageiros
           // textViewPassageiroDe.setText("Passageiro " + Configuracoes.iteracaoPassageiros);


        }//fim do if para editar == 1
        buttonProsseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                erro = 0; //se acontecer algum erro no salvar ou editar, nao executa a passagem pra outra tela.
                passageiros.setV(eTPassageiroV.getText().toString());
                //nao precisa pegar placa, pois já está salvo na variavel.
                passageiros.setNome(eTNomePassageiro.getText().toString());
                passageiros.setDocumento(eTDocumentoPassageiro.getText().toString());
                passageiros.setNaturalidade(eTNaturalidadePassageiro.getText().toString());
                passageiros.setEstadoCivil(eTEstadoCivilPassageiro.getText().toString());
                passageiros.setTelefone(eTTelefonePassageiro.getText().toString());
                passageiros.setEscolaridade(eTEscolaridadePassageiro.getText().toString());
                passageiros.setEndereco(eTEnderecoPassageiro.getText().toString());
                passageiros.setOcupacao(eTOcupacaoPassageiro.getText().toString());
                if (checkBoxCintoPassageiro.isChecked()) {
                    passageiros.setCinto("S");
                }
                else {
                    passageiros.setCinto("N");
                }

                ContentValues valoresPassageiros = new ContentValues();
                valoresPassageiros.put("codigo", codigo);
                valoresPassageiros.put("codigopassageiros", Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros]);
                valoresPassageiros.put("placapassageiros", passageiros.getPlacaPassageiros());
                valoresPassageiros.put("v", passageiros.getV());
                valoresPassageiros.put("nome", passageiros.getNome());
                valoresPassageiros.put("documento", passageiros.getDocumento());
                valoresPassageiros.put("naturalidade", passageiros.getNaturalidade());
                valoresPassageiros.put("estadocivil", passageiros.getEstadoCivil());
                valoresPassageiros.put("telefone", passageiros.getTelefone());
                valoresPassageiros.put("escolaridade", passageiros.getEscolaridade());
                valoresPassageiros.put("endereco", passageiros.getEndereco());
                valoresPassageiros.put("ocupacao", passageiros.getOcupacao());
                valoresPassageiros.put("cinto", passageiros.getCinto());

                if (contadorButtonEditar) { // se for editar
                    try {
                        db.update(configuracoes.getTabelaPassageiros(), valoresPassageiros, "codigopassageiros = " + Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] + " ", null);
                        Toast.makeText(PassageirosActivity.this, "Passageiro " + Configuracoes.iteracaoPassageiros + " de V" + passageiros.getV() + " atualizado com sucesso... ", Toast.LENGTH_SHORT).show();
//
                    } //try para update
                    catch (Exception e) {
                        erro = 1; //recebe um para nao passar pra outra tela
                        mostrarMensagemBasica("Erro!", "Erro ao tentar atualizar: " + e.toString());
                    } //fim do catch para update
                }//fim do if para editar
                else {
                    try { //try para inserir dados
                        db.insert(configuracoes.getTabelaPassageiros(), null, valoresPassageiros);
                        Toast.makeText(PassageirosActivity.this, "Passageiro " + Configuracoes.iteracaoPassageiros + " de V" + passageiros.getV() + " inserido com sucesso... ", Toast.LENGTH_SHORT).show();
//                        mostrarMensagemBasica("OK", "Passageiro " + Configuracoes.iteracaoPassageiros + " de V" + passageiros.getV() + " inserido com sucesso... ");
                    }//fim do try
                    catch (Exception e) {
                        erro = 1; //recebe um para nao passar pra outra tela
                        mostrarMensagemBasica("Erro!", "Erro ao tentar salvar: " + e.toString());
                    } //fim do catch para inserir dados
                }
                if (erro == 0) {
                    int codigoTemp;
                    if (!contadorButtonEditar) {
                        codigoTemp = Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros];
                        Configuracoes.contadorCodigoPassageiros++;
                        Configuracoes.codigoPassageiros[Configuracoes.contadorCodigoPassageiros] = codigoTemp +1;
                    }else {
                        Configuracoes.contadorCodigoPassageiros++;
                    }

                    if (Configuracoes.iteracaoPassageiros <= quantidadeDePassageiros) { //até chegar na quantidade total dos passageiros
                        Configuracoes.iteracaoPassageiros++; //incrementa o passageiros
                        InicializaTelaPassageiros(); //vai carregando as telas de passageiros
                    } else {
                        iteracaoVeiculos(); //caso contrario, vai incrementar os veiculos
                    }//
                }

            }
        });

    }//fim do metodo para inclusao dos passageiros.

    //metodo para incrementar o veiculo passando pro proximo, ou então finalizar o acd.
    public void iteracaoVeiculos() {
        if (Configuracoes.iteracaoVeiculos < quantidadeDeVeiculos) { //se for menor ou igual, inclui os veiculos.
            Configuracoes.iteracaoVeiculos++; //incrementa os veiculos;
            Configuracoes.iteracaoPassageiros = 1; // redefine a quantidade de passageiros para posterior selecao.
            //Configuracoes.contadorCodigoPassageiros = 0;
            CarregaTelaVeiculos(); //abre a tela de veiculos para nova seleção.

        }
        else { //se chegar ao limite de veiculos, salva e conclui o acd.
            Configuracoes.contadorCodigoPassageiros = 0;// zerar para possiveis novas inserções
            mostrarMensagemBasicaSucesso("OK", "Processo concluído com sucesso... ");
            erro = 2;
        }
    }

    //carregar a tela de veiculos
    public void CarregaTelaVeiculos() {
        Intent intentVeiculos = new Intent(PassageirosActivity.this, VeiculosActivity.class); //carregar a tela de veiculos  do acd
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

    private void mostrarMensagemBasicaSucesso (String title, String mensagem)  {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.close();
                LimparTelas();
            }
        });
        alerta.setTitle(title).setMessage(mensagem).show();
    }

    //vai limpar as telas da memoria e iniciar a tela inicial
    private void LimparTelas () {
        Intent intent = new Intent(getApplicationContext(), BRBrasilisActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() { //fechar todas as telas, e abrir a primeira

        if (erro == 2) {
            LimparTelas();
        }
        else if (erro == 0 && !contadorButtonEditar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sair?");
            builder.setMessage("Se voltar, todo o seu cadastro será perdido. Deseja?");
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
        }

    }
}
