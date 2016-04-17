package br.com.marcell.brbrasilis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Marcell on 10/03/2016.
 */
public class Configuracoes extends Activity{

    public final String versao = "1.0";
    public final String programa = "BRBrasilis";

    //variaveis relativos ao BD
    private String tabelaInformacoesBasicas = "tabelainformacoesbasicas";
    private String tabelaVeiculos = "tabelaveiculos";
    private String tabelaCondutor = "tabelacondutor";
    private String tabelaPassageiros = "tabelapassageiros";
    private String nomeBanco = "bancoBRBrasilis"; //nome do banco de dados
    private String siteAtualizacaoAPK = "https://www.dropbox.com/sh/d2v82n7vsjoupgl/AADzDOmFWk-SC8HdZPfwu4Uxa?dl=0"; //site do mega

    public String getSiteAtualizacaoAPK() {
        return siteAtualizacaoAPK;
    }

    public void setSiteAtualizacaoAPK(String siteAtualizacaoAPK) {
        this.siteAtualizacaoAPK = siteAtualizacaoAPK;
    }

    public static int iteracaoVeiculos = 1; //iterar os veiculos para saber se chegou ao fim
    public static int iteracaoPassageiros = 1; //iterar passageiros
    public static int[] codigoPassageiros = new int[50]; //codigo da PK do BD tabela dos passageiros.
    public static int contadorCodigoPassageiros = 0; //contador para o array receber os codigos dos passa referentes a V alguma coisa e codigo algum


    public static int getIteracaoPassageiros() {
        return iteracaoPassageiros;
    }

    public static void setIteracaoPassageiros(int iteracaoPassageiros) {
        Configuracoes.iteracaoPassageiros = iteracaoPassageiros;
    }


    public static int getIteracaoVeiculos() {
        return iteracaoVeiculos;
    }

    public static void setIteracaoVeiculos(int iteracaoVeiculos) {
        Configuracoes.iteracaoVeiculos = iteracaoVeiculos;
    }

    public String getTabelaInformacoesBasicas() {
        return tabelaInformacoesBasicas;
    }

    public String getTabelaVeiculos() {
        return tabelaVeiculos;
    }

    public String getTabelaCondutor() {
        return tabelaCondutor;
    }

    public String getTabelaPassageiros() {
        return tabelaPassageiros;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void pegarDataAtual (EditText editText) {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        editText.setText(dateString);
    } //fim do pegarDataAtual

    public void pegarHoraAtual (EditText editText) {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String horaString = sdfHora.format(date);
        editText.setText(horaString);

    } //fim do pegarHoraAtual

    public String inserirDataBD(String data) {
        Date d = new Date() ; //tem q deixar
        SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd"); //seta o formato de data atual
        return (data = formatador.format(d)); //setar a data atual para ser gravada no BD

    }//fim do metodo inserir data BD

    public String trocarTraco (String valor) {
        valor = valor.replaceAll("-", "/");
        return valor;
    }



}
