package br.com.marcell.brbrasilis;

/**
 * Created by Marcell on 30/03/2016.
 */
public class InformacoesBasicas {

    private int codigo;
//    private String placa;
//    private String v;
    private String br;
    private String km;
    private String sentido;
    private String data;
    private String hora;
    private String quantidadeDeVeiculos;

//    public InformacoesBasicas (int codigo) {
//        this.codigo = codigo;
//
////    }
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getQuantidadeDeVeiculos() {
        return quantidadeDeVeiculos;
    }

    public void setQuantidadeDeVeiculos(String quantidadeDeVeiculos) {
        this.quantidadeDeVeiculos = quantidadeDeVeiculos;
    }
}
