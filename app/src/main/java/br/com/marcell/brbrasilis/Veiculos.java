package br.com.marcell.brbrasilis;

/**
 * Created by Marcell on 31/03/2016.
 */
public class Veiculos {
    private int codigo;
    private String v;
    private String placa;
    private String quantidadeDePassageirosSTR;
    private int quantidadeDePassageiros;

    public int getQuantidadeDePassageiros() {
        return quantidadeDePassageiros;
    }

    public void setQuantidadeDePassageiros(int quantidadeDePassageiros) {
        this.quantidadeDePassageiros = quantidadeDePassageiros;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getQuantidadeDePassageirosSTR() {
        return quantidadeDePassageirosSTR;
    }

    public void setQuantidadeDePassageirosSTR(String quantidadeDePassageirosSTR) {
        this.quantidadeDePassageirosSTR = quantidadeDePassageirosSTR;
    }
}
