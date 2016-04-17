package br.com.marcell.brbrasilis;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CondutorActivity extends Activity {

    int codigo;
    Boolean contadorButtonEditar= false;

    SQLiteDatabase db;
    Cursor c;

    Configuracoes configuracoes = new Configuracoes();
    Condutor condutor = new Condutor();

    int quantidadeDeVeiculos; //recebe a quantidade de veiculos envolvidos no acd atraves da selecao no spinner
    int quantidadeDePassageiros; //vai receber a quantidade de passagerios de cada veiculo

    //tela de condutores
    EditText eTCondutorV;
    Button buttonPassageiros;
    EditText eTNomeCondutor, eTCPFCondutor, eTNaturalidadeCondutor, eTTelefoneCondutor;
    EditText eTEnderecoCondutor, eTOcupacaoCondutor, eTEtilometroCondutor;
    CheckBox checkBoxCintoCondutor;
    TextView textViewCPF;
    AutoCompleteTextView eTEstadoCivilCondutor, eTEscolaridadeCondutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condutor);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Habilita novos recursos na janela dizendo que terá recursos e será um icone a esquerda
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        // Adiciona um ícone a esquerda
//        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.transport);
        ReceberVariaveis();
        InicializaTelaCondutor();
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
            condutor.setPlacaCondutor(i.getStringExtra("placa"));
        }
        catch (Exception e) {
            mostrarMensagemBasica("Erro", "Erro ao inicializar variáveis de Condutor." + e.toString());
        }
    } //fim do receberVariaveis

    public void InicializaTelaCondutor() {
        setContentView(R.layout.activity_condutor);
        eTCondutorV = (EditText) findViewById(R.id.eTCondutorV);
        buttonPassageiros = (Button) findViewById(R.id.buttonPassageiros);
        eTNomeCondutor = (EditText) findViewById(R.id.eTNomeCondutor);
        eTCPFCondutor = (EditText) findViewById(R.id.eTCPFCondutor);
        eTNaturalidadeCondutor = (EditText) findViewById(R.id.eTNaturalidadeCondutor);
        eTEstadoCivilCondutor = (AutoCompleteTextView) findViewById(R.id.eTEstadoCivilCondutor);
        eTTelefoneCondutor = (EditText) findViewById(R.id.eTTelefoneCondutor);
        eTEscolaridadeCondutor = (AutoCompleteTextView) findViewById(R.id.eTEscolaridadeCondutor);
        eTEnderecoCondutor = (EditText) findViewById(R.id.eTEnderecoCondutor);
        eTOcupacaoCondutor = (EditText) findViewById(R.id.eTOcupacaoCondutor);
        checkBoxCintoCondutor = (CheckBox) findViewById(R.id.checkBoxCintoCondutor);
        eTEtilometroCondutor = (EditText) findViewById(R.id.eTEtilometroCondutor);
        textViewCPF = (TextView) findViewById(R.id.textViewCPF);

        ArrayAdapter arrayEstadoCivil = ArrayAdapter.createFromResource(this, R.array.opcoesEstadoCivil, android.R.layout.select_dialog_item);
        eTEstadoCivilCondutor.setThreshold(1);
        eTEstadoCivilCondutor.setAdapter(arrayEstadoCivil);

        ArrayAdapter arrayEscolaridade = ArrayAdapter.createFromResource(this, R.array.opcoesEscolaridade, android.R.layout.select_dialog_item);
        eTEscolaridadeCondutor.setThreshold(1);
        eTEscolaridadeCondutor.setAdapter(arrayEscolaridade);

        eTCondutorV.setEnabled(false);//sempre falso para edição.
        eTCondutorV.setText(String.valueOf(Configuracoes.iteracaoVeiculos));

        if (contadorButtonEditar) { //preencher os dados para alteração
            String SQL = "Select * from " +configuracoes.getTabelaCondutor() + " where codigo = " + codigo + " and v = " + Configuracoes.iteracaoVeiculos;
            try {
                abrirConexao();
                c = db.rawQuery(SQL, null);
                c.moveToFirst();
                eTCondutorV.setText(c.getString(c.getColumnIndexOrThrow("v")));
                eTNomeCondutor.setText(c.getString(c.getColumnIndexOrThrow("nome")));
                eTCPFCondutor.setText(c.getString(c.getColumnIndexOrThrow("CPF")));
                eTNaturalidadeCondutor.setText(c.getString(c.getColumnIndexOrThrow("naturalidade")));
                eTEstadoCivilCondutor.setText(c.getString(c.getColumnIndexOrThrow("estadocivil")));
                eTTelefoneCondutor.setText(c.getString(c.getColumnIndexOrThrow("telefone")));
                eTEscolaridadeCondutor.setText(c.getString(c.getColumnIndexOrThrow("escolaridade")));
                eTEnderecoCondutor.setText(c.getString(c.getColumnIndexOrThrow("endereco")));
                eTOcupacaoCondutor.setText(c.getString(c.getColumnIndexOrThrow("ocupacao")));
                eTEtilometroCondutor.setText(c.getString(c.getColumnIndexOrThrow("etilometro")));
                if (c.getString(c.getColumnIndexOrThrow("cinto")).equals("S")) {
                    checkBoxCintoCondutor.setChecked(true);
                }else {
                    checkBoxCintoCondutor.setChecked(false);
                }

            }//fim do try
            catch (Exception e) {

            } //fim do catch

        } //fim do editar == 1
        buttonPassageiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //checar informações , salva e abrir tela da inclusão dos passageiros.
                condutor.setV(eTCondutorV.getText().toString());
                //nao precisa pegar placa, pois já está salvo na variavel.
                condutor.setNome(eTNomeCondutor.getText().toString());
                condutor.setCPF(eTCPFCondutor.getText().toString());
                condutor.setNaturalidade(eTNaturalidadeCondutor.getText().toString());
                condutor.setEstadoCivil(eTEstadoCivilCondutor.getText().toString());
                condutor.setTelefone(eTTelefoneCondutor.getText().toString());
                condutor.setEscolaridade(eTEscolaridadeCondutor.getText().toString());
                condutor.setEndereco(eTEnderecoCondutor.getText().toString());
                condutor.setOcupacao(eTOcupacaoCondutor.getText().toString());
                condutor.setEtilometro(eTEtilometroCondutor.getText().toString());

                int erro = 0; //se acontecer algum erro no salvar ou editar, nao executa a passagem pra outra tela.
                if (checkBoxCintoCondutor.isChecked()) {
                    condutor.setCinto("S");
                }
                else {
                    condutor.setCinto("N");
                }
                if (eTCPFCondutor.getText().toString().isEmpty()){ //se cpf estiver vazio, entra aqui
                    mostrarMensagemBasica("Erro!", "Campo CPF não pode ficar em branco.");
                    textViewCPF.setTextColor(Color.RED);
                }
                else {
                    ContentValues valoresCondutor = new ContentValues();
                    valoresCondutor.put("codigo", codigo);
                    valoresCondutor.put("placacondutor", condutor.getPlacaCondutor());
                    valoresCondutor.put("v", condutor.getV());
                    valoresCondutor.put("nome", condutor.getNome());
                    valoresCondutor.put("CPF", condutor.getCPF());
                    valoresCondutor.put("naturalidade", condutor.getNaturalidade());
                    valoresCondutor.put("estadocivil", condutor.getEstadoCivil());
                    valoresCondutor.put("telefone", condutor.getTelefone());
                    valoresCondutor.put("escolaridade", condutor.getEscolaridade());
                    valoresCondutor.put("endereco", condutor.getEndereco());
                    valoresCondutor.put("ocupacao", condutor.getOcupacao());
                    valoresCondutor.put("cinto", condutor.getCinto());
                    valoresCondutor.put("etilometro", condutor.getEtilometro());

                    if (contadorButtonEditar) { //se for editar
                        try {
                            db.update(configuracoes.getTabelaCondutor(), valoresCondutor, "codigo = " + codigo + " and v = " + Configuracoes.iteracaoVeiculos, null);
                            Toast.makeText(CondutorActivity.this, "Condutor de V" + condutor.getV() + " atualizado com sucesso... ", Toast.LENGTH_SHORT).show();
//                            mostrarMensagemBasica("OK", "Condutor de V" + condutor.getV() + " atualizado com sucesso... ");
                        } //try para update
                        catch (Exception e) {
                            erro = 1; //recebe um para nao passar pra outra tela
                            mostrarMensagemBasica("Erro!", "Erro ao tentar atualizar V" + condutor.getV() + ": " + e.toString());
                        } //fim do catch para update
                    }
                    else{ //se for salvar
                        try { //try para inserir dados
                            db.insert(configuracoes.getTabelaCondutor(), null, valoresCondutor);
                            Toast.makeText(CondutorActivity.this, "Condutor de V" + condutor.getV() + " inserido com sucesso... ", Toast.LENGTH_SHORT).show();
//                            mostrarMensagemBasica("OK", "Condutor de V" + condutor.getV() + " inserido com sucesso... ");
                        }
                        catch (Exception e) {
                            erro = 1; //recebe um para nao passar pra outra tela
                            mostrarMensagemBasica("Erro!", "Erro ao tentar salvar V" + condutor.getV() + ": " + e.toString());
                        } //fim do catch para inserir dados
                    }
                    if (erro == 0) {
                        textViewCPF.setTextColor(Color.BLACK);
                        Configuracoes.iteracaoPassageiros++; // somar mais um pois já incluiu o motorista.
                        if (quantidadeDePassageiros >=1) { //so entra na tela de quantidade de passageiros se for maior que um
                            CarregaTelaPassageiros();
                        }
                        else { //se for 0 finaliza o veiculo, passa pro proximo, ou entao finaliza o bat
                            iteracaoVeiculos();
                        }

                    }//fim do if de erro == 0
                }//fim do else para saber se o campo cpf esta vazio
            }
        });
        eTEtilometroCondutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CondutorActivity.this, "Colocar o número do Teste e o Resultado...", Toast.LENGTH_SHORT ).show();;
            }
        });
    } //fim do metodo CarregaTelaCondutor

    //carregar a tela de Passageiros
    public void CarregaTelaPassageiros() {
        Intent intentPassageiros = new Intent(CondutorActivity.this, PassageirosActivity.class); //carregar a tela de condutores  do acd
        Bundle bundle = new Bundle();
        bundle.putInt("codigo", codigo);
        bundle.putBoolean("contadorButtonEditar", contadorButtonEditar);
        bundle.putInt("quantidadeDeVeiculos", quantidadeDeVeiculos);
        bundle.putInt("quantidadeDePassageiros", quantidadeDePassageiros);
        bundle.putString("placa", condutor.getPlacaCondutor());
        intentPassageiros.putExtras(bundle); //passar o codigo pra intent que sera acessado na proxima activity
        startActivity(intentPassageiros);
    }

    //metodo para incrementar o veiculo passando pro proximo, ou então finalizar o acd.
    public void iteracaoVeiculos() { //metodo tambem está no passageiros
        if (Configuracoes.iteracaoVeiculos < quantidadeDeVeiculos) { //se for menor ou igual, inclui os veiculos.
            Configuracoes.iteracaoVeiculos++; //incrementa os veiculos;
            Configuracoes.iteracaoPassageiros = 1; // redefine a quantidade de passageiros para posterior selecao.
            CarregaTelaVeiculos(); //abre a tela de veiculos para nova seleção.

        }
        else { //se chegar ao limite de veiculos, salva e conclui o acd.
            mostrarMensagemBasicaSucesso("OK", "Processo concluído com sucesso... ");
        }
    }

    //carregar a tela de veiculos
    public void CarregaTelaVeiculos() {
        Intent intentVeiculos = new Intent(CondutorActivity.this, VeiculosActivity.class); //carregar a tela de veiculos  do acd
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
    }//fim do limpa telas

    @Override
    public void onBackPressed() { //fechar todas as telas, e abrir a primeira
        if (!contadorButtonEditar) {
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
        } else {
            finish();
        }

    }


}
